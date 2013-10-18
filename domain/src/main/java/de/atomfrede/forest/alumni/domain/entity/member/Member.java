package de.atomfrede.forest.alumni.domain.entity.member;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.activity.Activity;
import de.atomfrede.forest.alumni.domain.entity.company.Company;
import de.atomfrede.forest.alumni.domain.entity.contact.ContactData;
import de.atomfrede.forest.alumni.domain.entity.degree.Degree;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

//tblmitglied
@SuppressWarnings("serial")
@Entity
@Table(name = "member")
public class Member extends AbstractEntity {

	@Id
	private Long id;

	@OneToOne
	@JoinColumn(name = "degree", nullable = true)
	private Degree degree;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entrydate")
	private java.util.Date entryDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "leavedate")
	private java.util.Date leaveDate;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Activity> activities;

	@OneToOne
	@JoinColumn(name = "contactdata", nullable = true)
	private ContactData contactData;

	@OneToOne
	@JoinColumn(name = "sector", nullable = true)
	private Sector sector;

	@OneToOne
	@JoinColumn(name = "department", nullable = true)
	private Department department;

	@OneToOne
	@JoinColumn(name = "company", nullable = true)
	private Company company;

	@Column(name = "salutation")
	private String salutation;

	@Column(name = "title")
	private String title;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "profession")
	private String profession;

	@Column(name = "graduationyear")
	private String yearOfGraduation;

	@Override
	public Long getId() {
		return id;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public java.util.Date getEntryDate() {
		return (Date) entryDate.clone();
	}

	public void setEntryDate(java.util.Date entryDate) {
		this.entryDate = entryDate;
	}
	
	public java.util.Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(java.util.Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public ContactData getContactData() {
		return contactData;
	}

	public void setContactData(ContactData contactData) {
		this.contactData = contactData;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getYearOfGraduation() {
		return yearOfGraduation;
	}

	public void setYearOfGraduation(String yearOfGraduation) {
		this.yearOfGraduation = yearOfGraduation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void addActivity(Activity activity) {
		if (activities == null) {
			activities = new HashSet<Activity>();
		}
		activities.add(activity);
	}

	public void removeActivity(Activity activity) {
		if (activities != null) {
			activities.remove(activity);
		}
	}

	public void clearActivities() {
		if (activities != null) {
			activities.clear();
		}
	}
	
	public String getFullname() {
		return getFullname(false);
	}
	
	public String getFullname(boolean lastnameFirst) {
		if(lastnameFirst) {
			return lastname + " " + firstname;
		} else {
			return firstname + " " + lastname;
		}
	}

}
