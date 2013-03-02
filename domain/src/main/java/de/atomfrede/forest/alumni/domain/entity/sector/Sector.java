package de.atomfrede.forest.alumni.domain.entity.sector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

//tblbranche
@SuppressWarnings("serial")
@Entity
@Table(name = "sector")
public class Sector extends AbstractEntity{

	@Id
	private Long id;
	
	@Column(name="sector")
	private String sector;
	
	public void setId(Long id){
		this.id = id;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

}
