package de.atomfrede.forest.alumni.service.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.service.query.filter.Filter;

@SuppressWarnings("serial")
public class Query<T extends AbstractEntity> implements Serializable {

	protected Class<T> clazz;
	protected List<Filter> filters;
	protected List<Filter> or;
	protected List<Filter> and;
	protected List<SubQuery<T>> subQueries;

	public Query(Class<T> clazz) {
		this.clazz = clazz;
	}

	public void addFilter(Filter filter) {
		if (filters == null) {
			filters = new ArrayList<>();
		}
		filters.add(filter);
	}

	public void addOr(Filter filter) {
		if (or == null) {
			or = new ArrayList<>();
		}
		or.add(filter);
	}

	public void addAnd(Filter filter) {
		if (and == null) {
			and = new ArrayList<>();
		}
		and.add(filter);
	}

	public void reset() {
		filters.clear();
		or.clear();
		and.clear();
	}

	public void addSubQuery(SubQuery<T> subQuery) {
		if (subQueries == null) {
			subQueries = new ArrayList<>();
		}
		subQueries.add(subQuery);
	}

	@Override
	public String toString() {
		return toString(false);
	}

	public String toString(boolean htmlOutput) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		if (filters != null) {
			for (Filter f : filters) {
				count++;
				if (count == filters.size()) {
					sb.append(f.toString());
				} else {
					if (htmlOutput) {
						sb.append(f.toString() + "<br/>");
					} else {
						sb.append(f.toString() + " und ");
					}
				}
			}
		}
		return sb.toString();
	}

}
