package de.atomfrede.forest.alumni.application.wicket.query;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.entity.member.Member;
import de.atomfrede.forest.alumni.service.query.Query;
import de.atomfrede.forest.alumni.service.query.QueryService;

@SuppressWarnings("serial")
public class MemberQueryProvider implements IDataProvider<Member> {

	@SpringBean
	private QueryService queryService;

	@SuppressWarnings("rawtypes")
	private Query currentQuery;

	public MemberQueryProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<? extends Member> iterator(long first, long count) {
		@SuppressWarnings("unchecked")
		List<Member> result = (List<Member>) queryService
				.queryDatabase(currentQuery);

		int fromIndex = (int) first;
		int toIndex = (int) (first + count);
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (fromIndex >= result.size()) {
			fromIndex = result.size() - 1;
		}
		if (toIndex >= result.size()) {
			toIndex = result.size();
		}
		return result.subList(fromIndex, toIndex).iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public long size() {
		List<Member> result = (List<Member>) queryService
				.queryDatabase(currentQuery);
		return result.size();
	}

	@Override
	public IModel<Member> model(Member object) {
		return new AbstractEntityModel<Member>(object);
	}

	public void setQuery(Query query) {
		this.currentQuery = query;
	}

}
