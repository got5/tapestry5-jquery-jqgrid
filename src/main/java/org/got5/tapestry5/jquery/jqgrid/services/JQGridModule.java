//
// Copyright 2010 GOT5 (GO Tapestry 5)
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

package org.got5.tapestry5.jquery.jqgrid.services;

import static org.got5.tapestry5.jquery.jqgrid.JQGridSymbolConstants.*;



import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.got5.tapestry5.jquery.jqgrid.services.javascript.JQGridJavaScriptStack;



public class JQGridModule
{
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration)
    {
    	configuration.addInstance(JQGridJavaScriptStack.STACK_ID, JQGridJavaScriptStack.class); 
    }

   

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("jquery-jqgrid", "org.got5.tapestry5.jquery.jqgrid"));
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String,String> configuration)
    {
     
    }

   
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
       
        configuration.add(JQUERY_JQGRID_CORE_PATH, "classpath:org/got5/tapestry5/jquery/jqgrid/jquery.jqgrid.core");
        configuration.add(JQUERY_JQGRID_VERSION, "4.0.0");
        configuration.add(ADD_JQUERY_IN_JQGRIDSTACK, "false");
      
    }
    
    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
    {
        configuration.add("tap-jquery-jqgrid", "org/got5/tapestry5/jquery/jqgrid");
    }
    
   
}
