//
// Copyright 2011 GOT5 (GO Tapestry 5)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package org.got5.tapestry5.jquery.jqgrid.test;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Wait;

public abstract class JavascriptTestSuite extends SeleniumTestCase
{

    @BeforeMethod
    public void adjustSpeed()
    {
        // it seems that integration test are unstable when speed is set to 0
        setSpeed("200");
    }

  

       
    
    @Test
	public void testBASIC_ColumnChooser(){
		open("/BASIC_ColumnChooser");
		waitForJqGridScript();
		
		click("//*[@id='jqgh_jqGrid_firstName']");
        
		final String expectedText = "Albert";
        new Wait()
        {
            @Override
            public boolean until()
            {
            	return getText("//*[@id='0']/td[2]").contains(expectedText);
            }
        }.wait("Expected text is missing ["+expectedText+"]", 5000l);
	}
    
    @Test
	public void testBASIC_ColumnReordering(){
		open("/BASIC_ColumnReordering");
		waitForJqGridScript();
	}
    
    @Test
	public void testGrouping_changegrouping(){
		open("/grouping_changegrouping");
		waitForJqGridScript();
		
		select("//*[@id='chngroup']","label=First Name");
        
		final String expectedText = "Albert - 1 Item(s)";
        new Wait()
        {
            @Override
            public boolean until()
            {
            	return getText("//*[@id='jqgridghead_0_0']/td/b").contains(expectedText);
            }
        }.wait("Expected text is missing ["+expectedText+"]", 5000l);
	}
    
    @Test
   	public void testGrouping_groupedheaders(){
   		open("/grouping_groupedheaders");
   		waitForJqGridScript();
           
   		final String expectedText = "COMPOSER - 2 Item(s)";
           new Wait()
           {
               @Override
               public boolean until()
               {
               	return getText("//*[@id='jqgridghead_0_1']/td/b").contains(expectedText);
               }
           }.wait("Expected text is missing ["+expectedText+"]", 5000l);
   	}
    
    @Test
   	public void testGrouping_groupingRowsCollapsed(){
   		open("/grouping_groupingrowscollapsed");
   		waitForJqGridScript();
           
   		final String expectedText = "ui-icon-circlesmall-plus";
           new Wait()
           {
               @Override
               public boolean until()
               {
               	return isElementPresent("//*[@id='jqgridghead_0_0']/td/span[contains(@class,'ui-icon-circlesmall-plus')]");
               }
           }.wait("class is missing ["+expectedText+"]", 5000l);
   	}
    
    @Test
   	public void testGrouping_hideGroupingColumns(){
   		open("/grouping_hidegroupingColumns");
   		waitForJqGridScript();
           
   		final String expectedText = "ui-icon-circlesmall-minus";
           new Wait()
           {
               @Override
               public boolean until()
               {
               	return isElementPresent("//*[@id='jqgridghead_0_0']/td/span[contains(@class,'ui-icon-circlesmall-minus')]");
               }
           }.wait("class is missing ["+expectedText+"]", 5000l);
   	}
    
    @Test
    public void testGrouping_RtlSupport(){
   		open("/grouping_rtlsupport");
   		waitForJqGridScript();
           
   		final String expectedText = "tree-wrap-rtl";
           new Wait()
           {
               @Override
               public boolean until()
               {
               	return isElementPresent("//*[@id='jqgridghead_0_0']/td/span[contains(@class,'tree-wrap-rtl')]");
               }
           }.wait("class is missing ["+expectedText+"]", 5000l);
   	}
    
    @Test
    public void testSortableRows(){
   		open("/SortableRows");
   		waitForJqGridScript();
           
   		click("//*[@id='jqgh_jqGrid_occupation']");
        
		final String expectedText = "ARTIST";
        new Wait()
        {
            @Override
            public boolean until()
            {
            	return getText("//*[@id='0']/td[3]").contains(expectedText);
            }
        }.wait("Expected text is missing ["+expectedText+"]", 5000l);
   	}
    
    
    @Test
   	public void testGrouping_withArrayData(){
   		open("/grouping_simplegroupingwitharraydata");
   		waitForJqGridScript();
           
   		final String expectedText = "ARTIST";
           new Wait()
           {
               @Override
               public boolean until()
               {
            	 //*[@id="jqgridghead_0_0"]/td
               	return getText("//*[@id='jqgridghead_0_0']/td").contains(expectedText);
               }
           }.wait("Expected text is missing ["+expectedText+"]", 5000l);
   	}
    
    private void waitForJqGridScript(){
		new Wait() {
			
			@Override
			public boolean until() {
				return isElementPresent("//head/script[contains(@src,'jqgrid.js')]");
			}
		}.wait("The jqGrid JavaScript file is missing.", 5000l);
	}
}
