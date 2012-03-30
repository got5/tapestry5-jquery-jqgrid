(function($){
    
	T5.extendInitializers(function(){
		
		function jqGrid(specs){
			$("#" + specs.field).jqGrid(specs.params);
	    	
            $("#" + specs.field).jqGrid('navGrid',specs.params.pager,{edit:false,add:false,del:false});
		}
		
		return {
			jqGrid : jqGrid
		}
	});
	
	$.extend(T5, {
		jqGrid : {
			firstDay: 0,
			localized:false,
			initLocalization : function(localization) {
			
			}
		}
	});
			
})(jQuery);






