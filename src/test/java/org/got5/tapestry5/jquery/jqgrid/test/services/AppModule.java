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

package org.got5.tapestry5.jquery.jqgrid.test.services;

import static org.got5.tapestry5.jquery.jqgrid.JQGridSymbolConstants.ADD_JQUERY_IN_JQGRIDSTACK;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ApplicationStateContribution;
import org.apache.tapestry5.services.ApplicationStateCreator;
import org.got5.tapestry5.jquery.jqgrid.services.JQGridModule;
import org.got5.tapestry5.jquery.jqgrid.test.data.IDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.MockDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Track;
import org.got5.tapestry5.jquery.jqgrid.test.services.MusicLibraryParser;
import org.slf4j.Logger;



@SubModule(value = JQGridModule.class)
public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
    	//if you don't use don't want to use tapestry5-jquery
    	//and if you want to use protoype and jquery provided by jqgrid
    	//configuration.add(ADD_JQUERY_IN_JQGRIDSTACK, "true");
    	configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.COMBINE_SCRIPTS, "false");
        configuration.add(SymbolConstants.GZIP_COMPRESSION_ENABLED, "false");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,fr,de");
    }
    
    public void contributeApplicationStateManager(
			MappedConfiguration<Class, ApplicationStateContribution> configuration) {

		ApplicationStateCreator<IDataSource> creator = new ApplicationStateCreator<IDataSource>() {
			public IDataSource create() {
				return new MockDataSource();
			}
		};

		configuration.add(IDataSource.class, new ApplicationStateContribution(
				"session", creator));
	}
    
    public static MusicLibrary buildMusicLibrary(Logger log)
    {
        URL library = AppModule.class.getResource("iTunes.xml");

        final List<Track> tracks = new MusicLibraryParser(log).parseTracks(library);

        final Map<Long, Track> idToTrack = CollectionFactory.newMap();

        for (Track t : tracks)
        {
            idToTrack.put(t.getId(), t);
        }

        return new MusicLibrary()
        {
            public Track getById(long id)
            {
                Track result = idToTrack.get(id);

                if (result != null) return result;

                throw new IllegalArgumentException(String.format("No track with id #%d.", id));
            }

            public List<Track> getTracks()
            {
                return tracks;
            }

            public List<Track> findByMatchingTitle(String title)
            {
                String titleLower = title.toLowerCase();

                List<Track> result = CollectionFactory.newList();

                for (Track t : tracks)
                {
                    if (t.getTitle().toLowerCase().startsWith(titleLower)) result.add(t);
                }

                return result;
            }
        };
    }

}
