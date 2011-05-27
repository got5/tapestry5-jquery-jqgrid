function columnChooserClosure(id,pager){
	
	var columnChooser = function(){ 
		jQuery("#"+id).jqGrid('navButtonAdd',pager,{
			caption: "Columns",
			title: "Reorder Columns",
			onClickButton: function(){
				jQuery("#"+id).jqGrid('columnChooser');
			}	
		});
	}
	
	return columnChooser;
}

function changeGroupingClosure(id)
{
	var changeGrouping = function(){
		$("#"+id).bind('change',function(){
        	var vl = $(this).val();
        	if(vl) {
        		if(vl == "clear") {
        			jQuery("#jqgrid").jqGrid('groupingRemove',true);
        		} else {
        			jQuery("#jqgrid").jqGrid('groupingGroupBy',vl);
        		}
        	}
        });
	}
	
	return changeGrouping
}

function hideShowColumnClosure(){
	
	var hideShowColumn = function(){
		jQuery("#hcg").bind('click', function() {
			jQuery("#jqgrid").jqGrid('hideCol',["occupation"]);
		});
		jQuery("#scg").bind('click', function() {
			jQuery("#jqgrid").jqGrid('showCol',["occupation"]);
		});
	}
	return hideShowColumn;
}