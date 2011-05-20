(function($){
    
	
	/** Container of functions that may be invoked by the Tapestry.init() function. */
    $.extend(Tapestry.Initializer, {
        jqGrid: function(specs) {	
			
            $("#" + specs.field).jqGrid({        
               	url:specs.params.url,
            	datatype:specs.params.datatype,
               	colNames:specs.params.colNames,
               	colModel:specs.params.colModel,
               	rowNum:specs.params.rowNum,
               	rowList:[5,10,15],
               	pager: $("#"+specs.params.pager),
               	sortname: specs.params.sortname,
                viewrecords: true,
                sortorder: specs.params.sortorder,
                caption:specs.params.caption,
                editurl:specs.params.editurl, 
                rownumbers:specs.params.rownumbers
            });
            $("#" + specs.field).jqGrid('navGrid',"#"+specs.params.pager,{edit:false,add:false,del:false});
        }
    });
	
	$.extend(Tapestry, {
		jqGrid : {
			firstDay: 0,
			localized:false,
			initLocalization : function(localization) {
			
			}
		}
	});
			
})(jQuery);






