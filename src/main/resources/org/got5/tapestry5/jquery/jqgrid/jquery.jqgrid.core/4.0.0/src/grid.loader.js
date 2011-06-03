//This file should be used if you want to debug and develop
function jqGridInclude()
{
    var pathtojsfiles = "js/"; // need to be ajusted
    // set include to false if you do not want some modules to be included
    var modules = [
        { include: false, incfile:'i18n/grid.locale-en.js'}, // jqGrid translation
        { include: false, incfile:'grid.base.js'}, // jqGrid base
        { include: false, incfile:'grid.common.js'}, // jqGrid common for editing
        { include: false, incfile:'grid.formedit.js'}, // jqGrid Form editing
        { include: false, incfile:'grid.inlinedit.js'}, // jqGrid inline editing
        { include: false, incfile:'grid.celledit.js'}, // jqGrid cell editing
        { include: false, incfile:'grid.subgrid.js'}, //jqGrid subgrid
        { include: false, incfile:'grid.treegrid.js'}, //jqGrid treegrid
        { include: false, incfile:'grid.grouping.js'}, //jqGrid grouping
        { include: false, incfile:'grid.custom.js'}, //jqGrid custom 
        { include: false, incfile:'grid.tbltogrid.js'}, //jqGrid table to grid 
        { include: false, incfile:'grid.import.js'}, //jqGrid import
        { include: false, incfile:'jquery.fmatter.js'}, //jqGrid formater
        { include: false, incfile:'JsonXml.js'}, //xmljson utils
        { include: false, incfile:'grid.jqueryui.js'}, //jQuery UI utils
        { include: false, incfile:'grid.filter.js'} // filter Plugin
    ];
    var filename;
    for(var i=0;i<modules.length; i++)
    {
        if(modules[i].include === true) {
        	filename = pathtojsfiles+modules[i].incfile;
			if(jQuery.browser.safari) {
				jQuery.ajax({url:filename,dataType:'script', async:false, cache: true});
			} else {
				if (jQuery.browser.msie) {
					document.write('<script charset="utf-8" type="text/javascript" src="'+filename+'"></script>');
				} else {
					IncludeJavaScript(filename);
				}
			}
		}
    }
    function IncludeJavaScript(jsFile)
    {
    	var oHead = document.getElementsByTagName('head')[0];
        var oScript = document.createElement('script');
        oScript.setAttribute('type', 'text/javascript');
        oScript.setAttribute('language', 'javascript');
        oScript.setAttribute('src', jsFile);
        //oHead.appendChild(oScript);
    }
}
jqGridInclude();
