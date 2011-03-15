//
//Copyright 2010 GOT5 (GO Tapestry 5)
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//

package org.got5.tapestry5.jquery.jqgrid.components;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PropertyOverrides;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.data.GridPagerPosition;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.jqgrid.services.javascript.JQGridJavaScriptStack;

@Import(stack = JQGridJavaScriptStack.STACK_ID)
@Events("Data")
public class JQGrid implements ClientElement
{
    /**
     * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit
     * {@link GridDataSource}. For Lists and object arrays, a GridDataSource is created automatically as a wrapper
     * around the underlying List.
     */
    @Parameter(required = true, autoconnect = true)
    private GridDataSource source;

   
    /**
     * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide up
     * the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result
     * set.
     */
    @Parameter("25")
    private int rowsPerPage;

    /**
     * Defines where the pager (used to navigate within the "pages" of results) should be displayed: "top", "bottom",
     * "both" or "none".
     */
    @Parameter(value = "top", defaultPrefix = BindingConstants.LITERAL)
    private GridPagerPosition pagerPosition;

  

    /**
     * The model used to identify the properties to be presented and the order of presentation. The model may be
     * omitted, in which case a default model is generated from the first object in the data source (this implies that
     * the objects provided by the source are uniform). The model may be explicitly specified to override the default
     * behavior, say to reorder or rename columns or add additional columns. The add, include,
     * exclude and reorder
     * parameters are <em>only</em> applied to a default model, not an explicitly provided one.
     */
    @Parameter
    private BeanModel model;

    /**
     * The model parameter after modification due to the add, include, exclude and reorder parameters.
     */
    private BeanModel dataModel;

    /**
     * The model used to handle sorting of the Grid. This is generally not specified, and the built-in model supports
     * only single column sorting. The sort constraints (the column that is sorted, and ascending vs. descending) is
     * stored as persistent fields of the Grid component.
     */
    @Parameter
    private GridSortModel sortModel;

    /**
     * A comma-seperated list of property names to be added to the {@link org.apache.tapestry5.beaneditor.BeanModel}.
     * Cells for added columns will be blank unless a cell override is provided. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String add;

    /**
     * A comma-separated list of property names to be retained from the
     * {@link org.apache.tapestry5.beaneditor.BeanModel}.
     * Only these properties will be retained, and the properties will also be reordered. The names are
     * case-insensitive. This parameter is only used
     * when a default model is created automatically.
     */
    @SuppressWarnings("unused")
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String include;

    /**
     * A comma-separated list of property names to be removed from the {@link org.apache.tapestry5.beaneditor.BeanModel}
     * .
     * The names are case-insensitive. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String exclude;

    /**
     * A comma-separated list of property names indicating the order in which the properties should be presented. The
     * names are case insensitive. Any properties not indicated in the list will be appended to the end of the display
     * order. This parameter is only used
     * when a default model is created automatically.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String reorder;


    /**
     * If true, then the Grid will be wrapped in an element that acts like a
     * {@link org.apache.tapestry5.corelib.components.Zone}; all the paging and sorting links will refresh the zone,
     * repainting
     * the entire grid within it, but leaving the rest of the page (outside the zone) unchanged.
     */
    @Parameter
    private boolean inPlace;

   

    @Persist
    private Integer currentPage;

    @Persist
    private String sortColumnId;

    @Persist
    private Boolean sortAscending;

    @Inject
    private ComponentResources resources;

    @Inject
    private BeanModelSource modelSource;

    @Environmental
    private ClientBehaviorSupport clientBehaviorSupport;
 
	 /**
	* Request attribute set to true if localization for the client-side jqgrid has been
	* configured. Used to ensure
	* that this only occurs once, regardless of how many DateFields are on the page.
	*/
	 static final String LOCALIZATION_CONFIGURED_FLAG = "jquery.jqgrid.localization-configured";
	
	
	 @Environmental
	 private JavaScriptSupport support;
	
	
	 @Inject
	 private Request request;
	
	 @Inject
	 private Locale locale;
	
	 @Inject
	 private ComponentDefaultProvider defaultProvider;
	
	 @Inject
	 private JavaScriptSupport javaScriptSupport;
	 
	
	 @Inject
	 private Messages messages;
	 
	 private String clientId;

 
	 private static final String PAGE = "page";
	
	 private static final String SEARCH = "search";
	 private static final String SEARCH_FIELD = "searchField";
	 private static final String SEARCH_STRING = "searchString";
	 private static final String SEARCH_OPER = "searchOper";
	
	 private static final String ND = "nd";
	 private static final String ROWS = "rows";
	 private static final String SIDX = "sidx";
	 private static final String SORD = "sord";
 
 

 /**
* Ajax event handler, form client side to get the data to display
* to parse it according to the server-side format. 
* see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:retrieving_data for more details
*/
 @OnEvent(value="Data")
 JSONObject onData()
 {
     String page = request.getParameter(PAGE);
     
     int requestedPageNumber=Integer.parseInt(page);
     String search = request.getParameter(SEARCH);
     String searchField = request.getParameter(SEARCH_FIELD);
     String searchString = request.getParameter(SEARCH_STRING);
     String searchOper = request.getParameter(SEARCH_OPER);
     //searchField=tax&searchString=100&searchOper=gt
     
     
     String nd = request.getParameter(ND);
     
     String rowsSelected = request.getParameter(ROWS);
     int rowsPerPage=Integer.parseInt(rowsSelected);
     
     String sidx = request.getParameter(SIDX);
     String sord = request.getParameter(SORD);
     
     JSONObject response = new JSONObject();
     int records = source.getAvailableRows();
     
     int nbPages= records/rowsPerPage;
     int modulo = records%rowsPerPage;
     if(modulo>0) nbPages++;
     
     int startIndex=0 + (requestedPageNumber-1)*rowsPerPage;
     int endIndex= startIndex + rowsPerPage -1;
     if(endIndex>records-1) endIndex= records-1;
     
     response.put("page",requestedPageNumber);
     response.put("total", nbPages);
     
     response.put("records", records);
     
     List<SortConstraint> sortConstraints = new ArrayList();
     if(!sidx.isEmpty())
     {
    	 GridSortModel sortModel = getSortModel();
    	 ColumnSort colSort =sortModel.getColumnSort(sidx);
    	 if(sord.equals("asc")) setSortAscending(true);
    	 else setSortAscending(false);
    	 sortModel.updateSort(sidx);
    	 sortConstraints = sortModel.getSortConstraints();
     }
     source.prepare(startIndex, endIndex,sortConstraints );
     
     
     JSONArray rows = new JSONArray();
     
     for(int index=startIndex;index<=endIndex;index++)
     {	 
    	 JSONObject row = new JSONObject();
    	 row.put("id", index);
    	 JSONArray cell = new JSONArray();
    	 //Class c = dataSource.getRowType();
    	 Object obj = source.getRowValue(index);
    	 List<String> names = getDataModel().getPropertyNames();
    	 for (String name: names)
    	 {
    		 
    		 try
    		 {	
    			 Class c = obj.getClass();
    			 Field[] fs = c.getFields();
    			 Method[] ms = c.getMethods();
    			 boolean found = false;
    			 for(Method m : ms)
    			 {
    				 if(m.getName().equalsIgnoreCase("get"+name))
    				 {
    					 //todo parse date
    					 cell.put(m.invoke(obj).toString());
    					 found = true;
    					 break;
    				 }
    			 }	 
    			 if(!found)
    			 {
    				 for(Field f : fs)
        			 {
        				 if(f.getName().equalsIgnoreCase(name))
        				 {
        					 cell.put(f.get(obj));
        					 found = true;
        					 break;
        				 }
        			 }	 
    			 }
    			 if(!found) cell.put("undefined "+name);
    			
    		 }
    		 catch(Exception ex)
    		 {
    			 cell.put("exception for "+name);
    		 }
    	 }
    	 row.put("cell",cell);
    	 rows.put(row);
     }
     response.put("rows", rows);
     
     //{"page":"1","total":2,"records":"13",
     //"rows":[{"id":"13","cell":["13","2007-10-06","Client 3","1000.00","0.00","1000.00",null]},
     //		   {"id":"12","cell":["12","2007-10-06","Client 2","700.00","140.00","840.00",null]},
     //        {"id":"11","cell":["11","2007-10-06","Client 1","600.00","120.00","720.00",null]},
     //        {"id":"10","cell":["10","2007-10-06","Client 2","100.00","20.00","120.00",null]},
     //        {"id":"9","cell":["9","2007-10-06","Client 1","200.00","40.00","240.00",null]},
     //        {"id":"8","cell":["8","2007-10-06","Client 3","200.00","0.00","200.00",null]},
     //        {"id":"7","cell":["7","2007-10-05","Client 2","120.00","12.00","134.00",null]},
     //        {"id":"6","cell":["6","2007-10-05","Client 1","50.00","10.00","60.00",""]},
     //        {"id":"5","cell":["5","2007-10-05","Client 3","100.00","0.00","100.00","no tax at all"]},
     ///       {"id":"4","cell":["4","2007-10-04","Client 3","150.00","0.00","150.00","no tax"]}],
     
     JSONObject userdata = new JSONObject();
     response.put("userdata",userdata);
     //"userdata":{"amount":3220,"tax":342,"total":3564,"name":"Totals:"}}
     return response;
 }

 /**
* Ajax event handler, 
* the result.
*/
 JSONObject onEdit()
 {


     JSONObject response = new JSONObject();


     return response;
 }

 void beginRender(MarkupWriter writer)
 {
	 

     String clientId = getClientId();

     writer.element("table","id", clientId);
     writer.end();
     writer.element("div","id", "pager"+clientId);
     writer.end();
     
     
     JSONObject setup = new JSONObject();
     setup.put("field", clientId);
     
     /*url:'server.php?q=2',
  	   datatype: "json",*/
     
     JSONObject jqgridParams = new JSONObject();
     jqgridParams.put("url", resources.createEventLink("data").toAbsoluteURI());
     jqgridParams.put("datatype", "json");
     
     //colNames:['Inv No','Date', 'Client', 'Amount','Tax','Total','Notes'],
     JSONArray colNames = new JSONArray();
     List<String> names = getDataModel().getPropertyNames();
     for (String name: names)
    	   colNames.put(name);
     jqgridParams.put("colNames", colNames);
     
     /*colModel:[
       		{name:'id',index:'id', width:55},
       		{name:'invdate',index:'invdate', width:90},
       		{name:'name',index:'name', width:100},
       		{name:'amount',index:'amount', width:80, align:"right"},
       		{name:'tax',index:'tax', width:80, align:"right"},		
       		{name:'total',index:'total', width:80,align:"right"},		
       		{name:'note',index:'note', width:150, sortable:false}		
       	],*/
     
     JSONArray colModel = new JSONArray();
     for (String name: names)
     {
    	 JSONObject model = new JSONObject();
    	 model.put("name", name);
    	 model.put("index", name);
    	 model.put("width", 100);
    	 colModel.put(model);
    	 //is this column sorted?
    	 if(sortAscending!=null)
    	 {
	    	 ColumnSort colSort =getSortModel().getColumnSort(name);
	    	 if(colSort != ColumnSort.UNSORTED)
	    	 {
	    		 //sortname: 'id',
	    		 jqgridParams.put("sortname", name);
	    		 //sortorder: "desc",
	    		 if(colSort == ColumnSort.ASCENDING) jqgridParams.put("sortorder", "asc");
	    		 else jqgridParams.put("sortorder", "dsc");
	    	 }
    	 }
     }
     jqgridParams.put("colModel", colModel);
     
 	 //rowNum:10,
     jqgridParams.put("rowNum", rowsPerPage);
     
     //rowList:[10,20,30],
     
     //pager: '#pager5',
     jqgridParams.put("pager","pager"+clientId);
    
     //viewrecords: true,
     
     //caption:"Simple data manipulation",
     jqgridParams.put("caption","Simple data manipulation");
   
     //editurl:"someurl.php" 
     jqgridParams.put("editurl", resources.createEventLink("edit").toAbsoluteURI());
     setup.put("params",jqgridParams);
    
     if (request.getAttribute(LOCALIZATION_CONFIGURED_FLAG) == null)
     {
         JSONObject spec = new JSONObject();
         request.setAttribute(LOCALIZATION_CONFIGURED_FLAG, true);
     }

     support.addInitializerCall("jqGrid", setup);
 }


 
 public String getClientId() {

     if (clientId == null) {

         clientId = javaScriptSupport.allocateClientId(resources);
     }

     return clientId;
 }

 /**
  * Defines where block and label overrides are obtained from. By default, the Grid component provides block
  * overrides (from its block parameters).
  */
 @Parameter(value = "this", allowNull = false)
 @Property(write = false)
 private PropertyOverrides overrides;
 
 /**
  * A version of GridDataSource that caches the availableRows property. This addresses TAPESTRY-2245.
  */
 static class CachingDataSource implements GridDataSource
 {
     private final GridDataSource delegate;

     private boolean availableRowsCached;

     private int availableRows;

     CachingDataSource(GridDataSource delegate)
     {
         this.delegate = delegate;
     }

     public int getAvailableRows()
     {
         if (!availableRowsCached)
         {
             availableRows = delegate.getAvailableRows();
             availableRowsCached = true;
         }

         return availableRows;
     }

     public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints)
     {
         delegate.prepare(startIndex, endIndex, sortConstraints);
     }

     public Object getRowValue(int index)
     {
         return delegate.getRowValue(index);
     }

     public Class getRowType()
     {
         return delegate.getRowType();
     }
 }

 /**
  * Default implementation that only allows a single column to be the sort column, and stores the sort information as
  * persistent fields of the Grid component.
  */
 class DefaultGridSortModel implements GridSortModel
 {
     public ColumnSort getColumnSort(String columnId)
     {
         if (!TapestryInternalUtils.isEqual(columnId, sortColumnId))
             return ColumnSort.UNSORTED;

         return getColumnSort();
     }

     private ColumnSort getColumnSort()
     {
         return getSortAscending() ? ColumnSort.ASCENDING : ColumnSort.DESCENDING;
     }

     public void updateSort(String columnId)
     {
         assert InternalUtils.isNonBlank(columnId);
         if (columnId.equals(sortColumnId))
         {
             setSortAscending(!getSortAscending());
             return;
         }

         sortColumnId = columnId;
         setSortAscending(true);
     }

     public List<SortConstraint> getSortConstraints()
     {
         if (sortColumnId == null)
             return Collections.emptyList();

         PropertyModel sortModel = getDataModel().getById(sortColumnId);

         SortConstraint constraint = new SortConstraint(sortModel, getColumnSort());

         return Collections.singletonList(constraint);
     }

     public void clear()
     {
         sortColumnId = null;
     }
 }

 GridSortModel defaultSortModel()
 {
     return new DefaultGridSortModel();
 }

 /**
  * Returns a {@link org.apache.tapestry5.Binding} instance that attempts to identify the model from the source
  * parameter (via {@link org.apache.tapestry5.grid.GridDataSource#getRowType()}. Subclasses may override to provide
  * a different mechanism. The returning binding is variant (not invariant).
  * 
  * @see BeanModelSource#createDisplayModel(Class, org.apache.tapestry5.ioc.Messages)
  */
 protected Binding defaultModel()
 {
     return new AbstractBinding()
     {
         public Object get()
         {
             // Get the default row type from the data source

             GridDataSource gridDataSource = source;

             Class rowType = gridDataSource.getRowType();

             if (rowType == null)
                 throw new RuntimeException(
                         String.format(
                                 "Unable to determine the bean type for rows from %s. You should bind the model parameter explicitly.",
                                 gridDataSource));

             // Properties do not have to be read/write

             return modelSource.createDisplayModel(rowType, overrides.getOverrideMessages());
         }

         /**
          * Returns false. This may be overkill, but it basically exists because the model is
          * inherently mutable and therefore may contain client-specific state and needs to be
          * discarded at the end of the request. If the model were immutable, then we could leave
          * invariant as true.
          */
         @Override
         public boolean isInvariant()
         {
             return false;
         }
     };
 }


 
 

 public BeanModel getDataModel()
 {
     if (dataModel == null)
     {
         dataModel = model;

         BeanModelUtils.modify(dataModel, add, include, exclude, reorder);
     }

     return dataModel;
 }

 

 public GridSortModel getSortModel()
 {
     return sortModel;
 }

 /*public Object getPagerTop()
 {
     return pagerPosition.isMatchTop() ? pager : null;
 }

 public Object getPagerBottom()
 {
     return pagerPosition.isMatchBottom() ? pager : null;
 }

 public int getCurrentPage()
 {
     return currentPage == null ? 1 : currentPage;
 }

 public void setCurrentPage(int currentPage)
 {
     this.currentPage = currentPage;
 }*/

 private boolean getSortAscending()
 {
     return sortAscending != null && sortAscending.booleanValue();
 }

 private void setSortAscending(boolean sortAscending)
 {
     this.sortAscending = sortAscending;
 }

 public int getRowsPerPage()
 {
     return rowsPerPage;
 }
 
}


