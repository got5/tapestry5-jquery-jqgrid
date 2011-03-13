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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidationSupport;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ComponentDefaultProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.jqgrid.services.javascript.JQGridJavaScriptStack;

@Import(stack = JQGridJavaScriptStack.STACK_ID)
@Events("Data")
public class JQGrid implements ClientElement
{
 /**
* The value parameter of a GridDataSource must be a {@link org.apache.tapestry5.grid.GridDataSource}.
*/
 @Parameter(required = true, principal = true, autoconnect = true)
 private GridDataSource dataSource;

 /**
  * A wrapper around the provided GridDataSource that caches access to the availableRows property. This is the source
  * provided to sub-components.
  */
 private GridDataSource cachingSource;

 /**
  * The number of rows of data displayed on each page. If there are more rows than will fit, the Grid will divide up
  * the rows into "pages" and (normally) provide a pager to allow the user to navigate within the overall result
  * set.
  */
 @Parameter("25")
 private int rowsPerPage; 
 
 /**
  * The model used to identify the properties to be presented and the order of presentation. The model may be
  * omitted, in which case a default model is generated from the first object in the data source (this implies that
  * the objects provided by the source are uniform). The model may be explicitly specified to override the default
  * behavior, say to reorder or rename columns or add additional columns. The add, include,
  * exclude and reorder
  * parameters are <em>only</em> applied to a default model, not an explicitly provided one.
  */
 @Parameter(required = true)
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
* Request attribute set to true if localization for the client-side jqgrid has been
* configured. Used to ensure
* that this only occurs once, regardless of how many DateFields are on the page.
*/
 static final String LOCALIZATION_CONFIGURED_FLAG = "jquery.jqgrid.localization-configured";




 @Environmental
 private JavaScriptSupport support;

 @Environmental
 private ValidationTracker tracker;

 @Inject
 private ComponentResources resources;

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

 private static final String RESULT = "result";

 private static final String ERROR = "error";
 private static final String PAGE = "page";



 

 /**
* Ajax event handler, form client side to get the data to display
* to parse it according to the server-side format. 
*/
 @OnEvent(value="Data")
 JSONObject onData()
 {
     String page = request.getParameter(PAGE);
     JSONObject response = new JSONObject();
     int records = dataSource.getAvailableRows();
     int pageNumber=Integer.parseInt(page);
     int nbPages= records/rowsPerPage;
     
     int startIndex=0 + (pageNumber-1)*rowsPerPage;
     int endIndex= startIndex + rowsPerPage ;
     response.put("page",pageNumber);
     response.put("total", nbPages);
     
     response.put("records", records);
     
     List<SortConstraint> sortConstraints = new ArrayList();
     dataSource.prepare(startIndex, endIndex, sortConstraints);
     
     
     JSONArray rows = new JSONArray();
     
     for(int index=startIndex;index< endIndex;index++)
     {	 
    	 JSONObject row = new JSONObject();
    	 row.put("id", index);
    	 JSONArray cell = new JSONArray();
    	 //Class c = dataSource.getRowType();
    	 Object obj = dataSource.getRowValue(index);
    	 List<String> names = model.getPropertyNames();
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
	 List<SortConstraint> sortConstraints = new ArrayList();

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
     List<String> names = model.getPropertyNames();
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
     }
     jqgridParams.put("colModel", colModel);
     
 	 //rowNum:10,
     jqgridParams.put("rowNum", rowsPerPage);
     
     //rowList:[10,20,30],
     
     //pager: '#pager5',
     jqgridParams.put("pager","pager"+clientId);
    
     //sortname: 'id',
     //viewrecords: true,
     //sortorder: "desc",
    
     //caption:"Simple data manipulation",
   
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
 
}


