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

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Celebrity;
import org.got5.tapestry5.jquery.jqgrid.test.data.CelebritySource;
import org.got5.tapestry5.jquery.jqgrid.test.data.IDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.MockDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.MusicLibraryDataSource;
import org.got5.tapestry5.jquery.jqgrid.test.data.Track;
import org.got5.tapestry5.jquery.jqgrid.test.services.MusicLibrary;

@Import(stylesheet = { "context:jquery-ui-1.7.3/jquery-ui-1.7.3.custom.css"})
public class Index
{
	@SessionState
	private IDataSource dataSource;
	
	/*@Persist
	private IDataSource dataSource;
	
	@SetupRender
	void onInit()
	{
		dataSource = new MockDataSource();
	}*/
	
	
	private Celebrity celebrity;
	private CelebritySource celebritySource;

	
	public GridDataSource getCelebritySource() {
		if(celebritySource==null)
			celebritySource = new CelebritySource(dataSource);
		return celebritySource;
	}

	public List<Celebrity> getAllCelebrities() {
		System.out.println("Getting all celebrities...");
		return dataSource.getAllCelebrities();
	}

	public Celebrity getCelebrity() {
		return celebrity;
	}
	
	public void setCelebrity(Celebrity celebrity) {
		this.celebrity = celebrity;
	}
	
	@Inject
	private ComponentResources resources;

	
	@Inject
	private BeanModelSource beanModelSource;
	
	@SuppressWarnings("unchecked")
	private BeanModel model;
	
	@SuppressWarnings("unchecked")
	public BeanModel getModel() {
		this.model = beanModelSource.createDisplayModel(Celebrity.class,resources.getMessages());
		return model;
	}
	
}
