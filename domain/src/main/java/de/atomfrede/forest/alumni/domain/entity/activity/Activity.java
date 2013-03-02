package de.atomfrede.forest.alumni.domain.entity.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.member.Member;

//tbltaetigkeit
@SuppressWarnings("serial")
@Entity
@Table(name = "activity")
public class Activity extends AbstractEntity {

	@Id
	private Long id;
	
	@Column(name="activity")
	private String activity;
	
	@Override
	public Long getId() {
		return id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
