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
package org.got5.tapestry5.jquery.jqgrid;


public class JQGridSymbolConstants {

    
    /**
     * Base path for jQuery jqgrid assets. this folder is assumed to be in
     * there.
     */
    public static final String JQUERY_JQGRID_CORE_PATH = "jquery.jqgrid.core.path";

    /**
     * The jQuery Grid version number. Must match the normal jQuery Grid folder name
     * pattern: <code>${jquery.jqgrid.core.path}/${jquery.jqgrid.version}</code>
     */
    public static final String JQUERY_JQGRID_VERSION = "jquery.jqgrid.version";
    
    /**
     * Indicates whether Tapestry is running in production mode or developer mode. The primary difference is how
     * exceptions are reported.
     */
    public static final String ADD_JQUERY_IN_JQGRIDSTACK = "jquery.jqgrid.addjquery";

}
