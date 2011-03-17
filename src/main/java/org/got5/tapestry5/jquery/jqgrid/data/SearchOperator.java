/**
 * 
 */
package org.got5.tapestry5.jquery.jqgrid.data;

/**
 * @author Got5
 *
 */
public enum SearchOperator
{
		bw ,//- begins with ( LIKE val% )
		eq ,//- equal ( = )
		ne ,//- not equal ( <> )
		lt ,//- little ( < )
		le ,//- little or equal ( <= )
		gt ,//- greater ( > )
		ge ,//- greater or equal ( >= )
		ew ,//- ends with (LIKE %val )
		cn  //- contain (LIKE %val% )
}