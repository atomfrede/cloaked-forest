package de.atomfrede.forest.alumni.domain.entity.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.department.Department;

//tbladressen
@SuppressWarnings("serial")
@Entity
@Table(name = "contactdata")
public class ContactData extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	@JoinColumn(name = "department", nullable = true)
	private Department department;

	@Column(name = "street")
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

	@Column(name = "phone")
	private String phone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "phoneD")
	private String phoneD;

	@Column(name = "faxD")
	private String faxD;

	@Column(name = "mobileD")
	private String mobileD;

	@Column(name = "emailD")
	private String emailD;

	@Column(name = "internetD")
	private String internetD;

	@Override
	public Long getId() {
		return id;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneD() {
		return phoneD;
	}

	public void setPhoneD(String phoneD) {
		this.phoneD = phoneD;
	}

	public String getFaxD() {
		return faxD;
	}

	public void setFaxD(String faxD) {
		this.faxD = faxD;
	}

	public String getMobileD() {
		return mobileD;
	}

	public void setMobileD(String mobileD) {
		this.mobileD = mobileD;
	}

	public String getEmailD() {
		return emailD;
	}

	public void setEmailD(String emailD) {
		this.emailD = emailD;
	}

	public String getInternetD() {
		return internetD;
	}

	public void setInternetD(String internetD) {
		this.internetD = internetD;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
