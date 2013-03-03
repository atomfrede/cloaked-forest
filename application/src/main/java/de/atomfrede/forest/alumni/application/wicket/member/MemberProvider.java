package de.atomfrede.forest.alumni.application.wicket.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.filter.FilterElement;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberProvider implements IDataProvider<Member>{
	
	@SpringBean
	MemberDao memberDao;
	
	long count;
	
	String nameFilter;
	
	public MemberProvider(){
		Injector.get().inject(this);
	}
	
	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Member> iterator(long offset, long count) {
		if(isFilteredByName()){
			FilterElement elem = new FilterElement().propertyName("lastname").filter(nameFilter);
			List<Member> members = memberDao.list(offset, count, elem);
			count = members.size();
			return members.iterator();
		}
		return memberDao.list(offset, count).iterator();
		
	}

	@Override
	public IModel<Member> model(Member object) {
		return new AbstractEntityModel<Member>(object);
	}

	@Override
	public long size() {
		if(isFilteredByName()){
			FilterElement elem = new FilterElement().propertyName("lastname").filter(nameFilter);
			List<Member> members = memberDao.list(0, memberDao.count(), elem);
			return members.size();
		}
		return memberDao.count();
	}
	
	private boolean isFilteredByName(){
		return (nameFilter != null && !"".equals(nameFilter.trim()));
	}

}
