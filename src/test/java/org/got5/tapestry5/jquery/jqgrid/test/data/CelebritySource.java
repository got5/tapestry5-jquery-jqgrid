package org.got5.tapestry5.jquery.jqgrid.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.internal.grid.CollectionGridDataSource;

import org.got5.tapestry5.jquery.jqgrid.data.FilteredGridDataSource;
import org.got5.tapestry5.jquery.jqgrid.data.SearchConstraint;
import org.got5.tapestry5.jquery.jqgrid.data.SearchOperator;
import org.got5.tapestry5.jquery.jqgrid.test.data.IDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Celebrity;

public class CelebritySource implements FilteredGridDataSource {

	private IDataSource dataSource;
    private List<Celebrity> celebrities;
    private CollectionGridDataSource cgds;

  
    
    public CelebritySource(IDataSource ds) {
        this.dataSource = ds;
        this.celebrities = dataSource.getAllCelebrities();
        this.cgds = new CollectionGridDataSource(dataSource.getAllCelebrities());
  
    }

    public int getAvailableRows() {
        //return dataSource.getAllCelebrities().size();
    	//return celebrities.size();
    	return this.cgds.getAvailableRows();
    }

    public void prepare(int indexFrom, int indexTo,List<SortConstraint> sortConstraints) {
        this.cgds.prepare(indexFrom, indexTo, sortConstraints);
    }

    public Object getRowValue(int i) {
        return this.cgds.getRowValue(i);
    }

    public Class getRowType() {
    	return this.cgds.getRowType();
    }
    
    public void resetFilter()
    {
    	this.cgds = new CollectionGridDataSource(dataSource.getAllCelebrities());
    }
    
	public void setFilter(List<SearchConstraint> searchConstraints)
	{
		List<Celebrity> start = new ArrayList<Celebrity>();
		start.addAll(celebrities);
		
		for (SearchConstraint search :searchConstraints) 
		{
			String propName= search.getPropertyName();
			PropertyConduit conduit = search.getConduit();
				
			for (Celebrity cel:celebrities ) 
			{
				Comparable val = (Comparable)conduit.get(cel);
				Comparable toCompare = (Comparable)search.getPropertyValue();
				int cp = toCompare.compareTo(val);
				
				if((search.getSearchOperator()==SearchOperator.eq && cp!=0) || //equal 
				   (search.getSearchOperator()==SearchOperator.lt && cp<=0)  || //less than
				   (search.getSearchOperator()==SearchOperator.le && cp<0) || //less equal
				   (search.getSearchOperator()==SearchOperator.gt && cp>=0)  ||
				   (search.getSearchOperator()==SearchOperator.ge && cp>0))
					start.remove(cel);
			}
		}
		
		this.cgds = new CollectionGridDataSource(start);
		
		
	}
	
}
