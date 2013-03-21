package de.atomfrede.forest.alumni.domain.entity.degree;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;

/**
 * Refers to the table tblabschluss
 * 
 * @author fred
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "degree")
public class Degree extends AbstractEntity {

	@Id
	protected Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "shortform")
	private String shortForm;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortForm() {
		return shortForm;
	}

	public void setShortForm(String shortForm) {
		this.shortForm = shortForm;
	}

	public String toString() {
		return title;
	}
}
