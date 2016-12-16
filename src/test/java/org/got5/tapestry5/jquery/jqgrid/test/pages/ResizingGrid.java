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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.got5.tapestry5.jquery.ImportJQueryUI;
import org.got5.tapestry5.jquery.jqgrid.components.JQGrid;
import org.got5.tapestry5.jquery.utils.JQueryTabData;


@ImportJQueryUI({"ie",
		"version",
		"widget",
		"data",
		"scroll-parent",
		"widgets/mouse", 
		"widgets/draggable",
		"widgets/resizable"})
@Import(library = { "context:static/js/demo.js"})
public class ResizingGrid extends Basic_Sample
{
	
	@Component
	private JQGrid jqGrid;
	
	@Property
	private List<JQueryTabData> listTabData;

	@SetupRender
	void onSetupRender()
	{
		listTabData = new ArrayList<JQueryTabData>();
	    listTabData.add(new JQueryTabData("Example","example"));
	}
	 
	public JSONObject getAdditionalParams(){
		
		JSONObject ap = new JSONObject();
		
		ap.put("width", "600");
		
		ap.put("rowNum","50");
		
		ap.put("rowList", "");
		
		ap.put("gridComplete", new JSONLiteral(String.format("resizingGridClosure('%s')",jqGrid.getClientId())));
		
		return ap;
		
	}
	
}
