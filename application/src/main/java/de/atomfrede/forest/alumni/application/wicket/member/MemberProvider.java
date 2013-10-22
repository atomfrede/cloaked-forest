package de.atomfrede.forest.alumni.application.wicket.member;

import java.util.Date;
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
public class MemberProvider implements IDataProvider<Member> {

	@SpringBean
	private MemberDao memberDao;

	private long count;

	private String nameFilter;
	
	private Date appointedDate;

	public MemberProvider() {
		Injector.get().inject(this);
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
	}

	@Override
	public Iterator<? extends Member> iterator(long offset, long count) {
		if (isFilteredByName() && isFilteredByDate()) {
			FilterElement elem = new FilterElement().propertyName("lastname")
					.filter(nameFilter);
			List<Member> members = memberDao.list(offset, count, appointedDate, elem);
			this.count = members.size();
			return members.iterator();
		} else if(isFilteredByName()) {
			FilterElement elem = new FilterElement().propertyName("lastname")
					.filter(nameFilter);
			List<Member> members = memberDao.list(offset, count, appointedDate, elem);
			this.count = members.size();
			return members.iterator();
		} else if(isFilteredByDate()) {
			List<Member> members = memberDao.list(offset, count, appointedDate);
			this.count = members.size();
			return members.iterator();
		}
		return memberDao.list(offset, count, "lastname", false).iterator();

	}

	@Override
	public IModel<Member> model(Member object) {
		return new AbstractEntityModel<Member>(object);
	}

	@Override
	public long size() {
		if (isFilteredByName() && isFilteredByDate()) {
			FilterElement elem = new FilterElement().propertyName("lastname")
					.filter(nameFilter);
			List<Member> members = memberDao.list(0, memberDao.count(), appointedDate, elem);
			return members.size();
		} else if(isFilteredByName()) {
			FilterElement elem = new FilterElement().propertyName("lastname")
					.filter(nameFilter);
			List<Member> members = memberDao.list(0, count, elem);
			return members.size();
		} else if(isFilteredByDate()) {
			List<Member> members = memberDao.list(0, count, appointedDate);
			return members.size();
		}
		return memberDao.count();
	}

	private boolean isFilteredByName() {
		return (nameFilter != null && !"".equals(nameFilter.trim()));
	}
	
	private boolean isFilteredByDate() {
		return appointedDate != null;
	}

	public String getNameFilter() {
		return nameFilter;
	}
	
	public Date getAppointedDate() {
		return appointedDate;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}
	
	public void setAppointedDate(Date appointedDate) {
		this.appointedDate = appointedDate;
	}

}
