//
// Copyright 2010 GOT5 (GO Tapestry 5)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.got5.tapestry5.jquery.jqgrid.services.javascript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.got5.tapestry5.jquery.jqgrid.JQGridSymbolConstants;


public class JQGridJavaScriptStack implements JavaScriptStack {

	public static final String STACK_ID = "jqGridStack";
	
	private final ThreadLocale threadLocale;
    
    private final AssetSource assetSource;

    private final boolean compactJSON;
    private final boolean addJqueryStack;

    private final List<Asset> javaScriptStack;
    private final List<Asset> jqueryStack;

    private final List<StylesheetLink> stylesheetStack;

    public JQGridJavaScriptStack(final ThreadLocale threadLocale,

            					 @Symbol(SymbolConstants.COMPACT_JSON)
            					 final boolean compactJSON,
    	
    							 @Symbol(SymbolConstants.PRODUCTION_MODE)
                                 final boolean productionMode,
                                 
                                 @Symbol(JQGridSymbolConstants.ADD_JQUERY_IN_JQGRIDSTACK)
                                 final boolean addJqueryStack,

                                 final AssetSource assetSource)
    {
    	this.threadLocale = threadLocale;
        this.assetSource = assetSource;
        this.compactJSON = compactJSON;
        this.addJqueryStack = addJqueryStack;

        final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>()
        {
            @Override
            public Asset map(String path)
            {
                return assetSource.getExpandedAsset(path);
            }
        };

        final Mapper<Asset, StylesheetLink> assetToStylesheetLink = new Mapper<Asset, StylesheetLink>()
        {
            @Override
            public StylesheetLink map(Asset input)
            {
                return new StylesheetLink(input);
            };
        };
        
        final Mapper<String, StylesheetLink> pathToStylesheetLink = pathToAsset.combine(assetToStylesheetLink);

        
        jqueryStack = F.flow("${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery.min.js",
                     	  	"${jquery.jqgrid.core.path}/jquery.noconflict.js",
                     	  	"${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery-ui-custom.min.js",
                     	  	"${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery.contextmenu.js",
                     	  	"${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery.layout.js",
                     	  	"${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery.tablednd.js",
                     	  	"${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/ui.multiselect.js"
                     	  	).map(pathToAsset).toList();


        if (!productionMode) {
        	
        	stylesheetStack = F.flow("${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/css/ui.jqgrid.css")
        	.map(pathToStylesheetLink).toList();

            javaScriptStack = F
                .flow("${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/jquery.jqgrid.min.js",             	  	 
    				  "${jquery.jqgrid.core.path}/jqgrid.js")
                	  
            .map(pathToAsset).toList();

        } else {
        	
        	stylesheetStack = F.flow("${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/css/ui.jqgrid.css")
        	.map(pathToStylesheetLink).toList();

            javaScriptStack = F
                .flow("${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.base.js", // jqGrid base
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.common.js", // jqGrid common for editing
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.formedit.js", // jqGrid Form editing
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.inlinedit.js", // jqGrid inline editing
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.celledit.js", // jqGrid cell editing
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.subgrid.js", //jqGrid subgrid
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.treegrid.js", //jqGrid treegrid
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.grouping.js", //jqGrid grouping
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.custom.js", //jqGrid custom 
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.tbltogrid.js", //jqGrid table to grid 
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.import.js", //jqGrid import
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/jquery.fmatter.js", //jqGrid formater
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/JsonXml.js", //xmljson utils
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.jqueryui.js", //jQuery UI utils
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.loader.js", //jqGrid loader
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.postext.js", 
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/grid.setcolumns.js",
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/jqDnr.js",   
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/jqModal.js", 
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/ui.multiselect.js",
                	  "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/src/jquery.searchFilter.js",   
                	  
                	  "${jquery.jqgrid.core.path}/jqgrid.js")
            .map(pathToAsset).toList();

        }

    }
    
   

    public String getInitialization()
    {
        Locale locale = threadLocale.getLocale();

        JSONObject spec = new JSONObject();
        // set language
        spec.put("language", locale.getLanguage());

        return null;
        //return String.format("Tapestry.JQGrid.initLocalization(%s);", spec.toString(compactJSON));
    }

    public List<Asset> getJavaScriptLibraries()
    {
    	Locale locale = threadLocale.getLocale();
        String pathToJQGridI18nRess = "${jquery.jqgrid.core.path}/${jquery.jqgrid.version}/js/i18n/grid.locale-"+locale.getLanguage()+".js";
        Asset jqGridI18nAsset = this.assetSource.getExpandedAsset(pathToJQGridI18nRess);
     	if (jqGridI18nAsset.getResource().exists())
     	{
     		List<Asset> ret = new ArrayList<Asset>();
     		if(addJqueryStack)
     			ret.addAll(jqueryStack);
     		//locale asset should be declare after file
     		// or you get this eror in firebug  (b.jgrid.getAccessor is not a function Line 53)
     		ret.add(jqGridI18nAsset); 
     		ret.addAll(javaScriptStack);
     		return ret;
     	}
    	return javaScriptStack;
    }

    public List<StylesheetLink> getStylesheets()
    {
        return stylesheetStack;
    }

    public List<String> getStacks()
    {
        return Collections.emptyList();
    }

}
