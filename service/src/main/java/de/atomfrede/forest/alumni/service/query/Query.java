package de.atomfrede.forest.alumni.service.query;

import java.util.ArrayList;
import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

public class Query<T extends AbstractEntity> {

	protected Class<T> clazz;
	protected List<Filter> filters;
	
	public Query(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public void addFilter(Filter filter){
		if(filters == null){
			filters = new ArrayList<>();
		}
		filters.add(filter);
	}
	
	public void reset(){
		filters.clear();
	}
}
