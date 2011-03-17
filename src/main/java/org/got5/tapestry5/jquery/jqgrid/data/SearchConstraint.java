/**
 * 
 */
package org.got5.tapestry5.jquery.jqgrid.data;

import org.apache.tapestry5.PropertyConduit;

/**
 * @author Got5
 *
 */
public class SearchConstraint {
	
	private String propertyName;
	private SearchOperator  searchOperator;
	private String propertyValue;
	private PropertyConduit conduit;
	
	
	
	public SearchConstraint(String propertyName, 
							SearchOperator searchOperator,
							String propertyValue,
							PropertyConduit conduit) {
		this.propertyName = propertyName;
		this.searchOperator = searchOperator;
		this.propertyValue = propertyValue;
		this.conduit = conduit;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public SearchOperator getSearchOperator() {
		return searchOperator;
	}
	public void setSearchOperator(SearchOperator searchOperator) {
		this.searchOperator = searchOperator;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public PropertyConduit getConduit() {
		return conduit;
	}
	public void setConduit(PropertyConduit conduit) {
		this.conduit = conduit;
	}
	
	
	

}
