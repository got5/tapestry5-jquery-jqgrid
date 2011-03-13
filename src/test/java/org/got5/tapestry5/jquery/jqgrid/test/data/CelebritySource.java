package org.got5.tapestry5.jquery.jqgrid.test.data;

import java.util.List;


import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import org.got5.tapestry5.jquery.jqgrid.test.data.IDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Celebrity;

public class CelebritySource implements GridDataSource {

	private IDataSource dataSource;
    private List<Celebrity> selection;
    private int indexFrom;
    
    public CelebritySource(IDataSource ds) {
        this.dataSource = ds;
    }

    public int getAvailableRows() {
        return dataSource.getAllCelebrities().size();
    }

    public void prepare(int indexFrom, int indexTo,List<SortConstraint> sortConstraints) {
        System.out.println("Preparing selection.");
        System.out.println("Index from " + indexFrom + 
          " to " + indexTo);
        
        selection = dataSource.getRange(indexFrom, indexTo);
        this.indexFrom = indexFrom;
    }

    public Object getRowValue(int i) {
        System.out.println("Getting value for row " + i);
        return selection.get(i - this.indexFrom);
    }

    public Class getRowType() {
        return Celebrity.class;
    }
}
