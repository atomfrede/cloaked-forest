package de.atomfrede.forest.alumni.domain.entity.department;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.company.Company;

//tblabteilung
@SuppressWarnings("serial")
@Entity
@Table(name = "department")
public class Department extends AbstractEntity {

	@Id
	private Long id;
	
	@Column(name = "department")
	private String department;
	
	@Column(name ="street")
	private String street;
	
	@Column(name = "number")
	private String number;
	
	@Column(name = "addon")
	private String addon;
	
	@Column(name = "postcode")
	private String postCode;
	
	@Column(name = "town")
	private String town;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "internet")
	private String internet;
	
	@Override
	public Long getId() {
		return id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAddon() {
		return addon;
	}

	public void setAddon(String addon) {
		this.addon = addon;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInternet() {
		return internet;
	}

	public void setInternet(String internet) {
		this.internet = internet;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
