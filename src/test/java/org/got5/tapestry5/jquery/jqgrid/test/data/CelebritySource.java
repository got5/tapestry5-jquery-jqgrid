package org.got5.tapestry5.jquery.jqgrid.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.SortConstraint;

import org.got5.tapestry5.jquery.jqgrid.data.FilteredGridDataSource;
import org.got5.tapestry5.jquery.jqgrid.data.SearchConstraint;
import org.got5.tapestry5.jquery.jqgrid.data.SearchOperator;
import org.got5.tapestry5.jquery.jqgrid.test.data.IDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Celebrity;

public class CelebritySource implements FilteredGridDataSource {

	private IDataSource dataSource;
    private List<Celebrity> selection;
    private List<Celebrity> celebrities;
    private int indexFrom;
  
    
    public CelebritySource(IDataSource ds) {
        this.dataSource = ds;
        this.celebrities = dataSource.getAllCelebrities();
  
    }

    public int getAvailableRows() {
        //return dataSource.getAllCelebrities().size();
    	return celebrities.size();
    }

    public void prepare(int indexFrom, int indexTo,List<SortConstraint> sortConstraints) {
        System.out.println("Preparing selection.");
        System.out.println("Index from " + indexFrom + 
          " to " + indexTo);
        for (SortConstraint constraint : sortConstraints) {
			final ColumnSort sort = constraint.getColumnSort();

			if (sort == ColumnSort.UNSORTED)
				continue;

			final PropertyConduit conduit = constraint.getPropertyModel().getConduit();

			final Comparator valueComparator = new Comparator<Comparable>() {
				public int compare(Comparable o1, Comparable o2) {
			
					if (o1 == o2)
						return 0;

					if (o2 == null)
						return 1;

					if (o1 == null)
						return -1;

					return o1.compareTo(o2);
				}
			};

			final Comparator rowComparator = new Comparator() {
				public int compare(Object row1, Object row2) {
					Comparable value1 = (Comparable) conduit.get(row1);
					Comparable value2 = (Comparable) conduit.get(row2);

					return valueComparator.compare(value1, value2);
				}
			};

			final Comparator reverseComparator = new Comparator() {
				public int compare(Object o1, Object o2) {
					int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;

					return modifier * rowComparator.compare(o1, o2);
				}
			};
			
			Collections.sort(celebrities, reverseComparator);
        }
        selection = getRange(indexFrom, indexTo);
        this.indexFrom = indexFrom;
    }

    public Object getRowValue(int i) {
        System.out.println("Getting value for row " + i);
        return selection.get(i - this.indexFrom);
    }

    public Class getRowType() {
        return Celebrity.class;
    }
    
    public void resetFilter()
    {
    	this.celebrities = dataSource.getAllCelebrities();
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
				String val = (String)conduit.get(cel);
				int cp = search.getPropertyValue().compareTo(val);
				if(cp!=0 && search.getSearchOperator()==SearchOperator.eq)
				start.remove(cel);
			}
		}
		
		this.celebrities = start;
		
		
	}
	
	public List<Celebrity> getRange(int indexFrom, int indexTo) {
		List<Celebrity> result = new ArrayList<Celebrity>();
		for (int i = indexFrom; i <= indexTo; i++) {
			result.add(celebrities.get(i));
		}
		return result;
	}
}
