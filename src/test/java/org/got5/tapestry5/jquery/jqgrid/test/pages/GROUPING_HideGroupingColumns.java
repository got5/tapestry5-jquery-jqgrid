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

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;

@Import(stylesheet = { "context:jquery-ui-1.7.3/jquery-ui-1.7.3.custom.css"})
public class GROUPING_HideGroupingColumns extends Basic_Sample
{

	public JSONObject getAdditionalParams(){
		
		JSONObject ap = new JSONObject();
		
		ap.put("width", "600");
		
		ap.put("grouping", true);
		
		ap.put("rowNum","50");
		
		ap.put("rowList", "");
		
		JSONObject grouping = new JSONObject();
		
		grouping.put("groupField",new JSONLiteral("['occupation']"));
		
		grouping.put("groupColumnShow", new  JSONLiteral("[false]"));
	
		ap.put("groupingView", grouping);
				
		return ap;
		
	}
	
}
