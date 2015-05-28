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
		
		click("//*[@id='jqgh_firstName']/span/span[1]");
        
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
        
		final String expectedText = "Ronald - 1 Item(s)";
        new Wait()
        {
            @Override
            public boolean until()
            {
            	return getText("//*[@id='jqgridghead_0']/td/b").contains(expectedText);
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
               	return getText("//*[@id='jqgridghead_1']/td/b").contains(expectedText);
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
