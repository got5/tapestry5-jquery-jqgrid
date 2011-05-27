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

package org.got5.tapestry5.jquery.jqgrid.test.pages;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.got5.tapestry5.jquery.jqgrid.components.JQGrid;


@ImportJQueryUI(value={
        "jquery.ui.mouse", "jquery.ui.draggable","jquery.ui.sortable","jquery.ui.dialog"})
@Import(library = { "context:static/js/demo.js"})
public class BASIC_ColumnChooser extends Basic_Sample
{
	
	@Inject 
	private JavaScriptSupport js;
	
	@Component
	private JQGrid jqGrid;
	
	public JSONObject getAdditionalParams(){
		
		JSONObject ap = new JSONObject();
		
		ap.put("width", "auto");
		
		ap.put("rowNum","50");
		
		ap.put("rowList", "");
		
		ap.put("gridComplete", new JSONLiteral(String.format("columnChooserClosure('%s','%s')",jqGrid.getClientId(), "#pager"+jqGrid.getClientId())));
		
		return ap;
		
	}
	
}
