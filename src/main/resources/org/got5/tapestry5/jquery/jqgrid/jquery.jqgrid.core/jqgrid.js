(function($){
    
	
	/** Container of functions that may be invoked by the Tapestry.init() function. */
    $.extend(Tapestry.Initializer, {
        jqGrid: function(specs) {
    	
    		$("#" + specs.field).jqGrid(specs.params);
    	
            $("#" + specs.field).jqGrid('navGrid',specs.params.pager,{edit:false,add:false,del:false});
            
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






