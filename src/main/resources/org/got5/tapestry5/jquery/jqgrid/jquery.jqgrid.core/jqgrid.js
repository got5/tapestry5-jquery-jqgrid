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
               	sortname: 'id',
                viewrecords: true,
                sortorder: "desc",
                caption:"Simple data manipulation",
                editurl:specs.params.editurl
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






