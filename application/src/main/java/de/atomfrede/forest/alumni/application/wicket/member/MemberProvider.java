package de.atomfrede.forest.alumni.application.wicket.member;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.atomfrede.forest.alumni.application.wicket.model.AbstractEntityModel;
import de.atomfrede.forest.alumni.domain.dao.member.MemberDao;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

@SuppressWarnings("serial")
public class MemberProvider implements IDataProvider<Member>{

	@SpringBean
	MemberDao memberDao;
	
	public MemberProvider(){
		Injector.get().inject(this);
	}
	
	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Member> iterator(long offset, long count) {
		return memberDao.list(offset, count).iterator();
	}

	@Override
	public IModel<Member> model(Member object) {
		return new AbstractEntityModel<Member>(object);
	}

	@Override
	public long size() {
		return memberDao.count();
	}

}
