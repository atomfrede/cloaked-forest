package de.atomfrede.forest.alumni.domain.entity.company;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.atomfrede.forest.alumni.domain.entity.AbstractEntity;
import de.atomfrede.forest.alumni.domain.entity.department.Department;
import de.atomfrede.forest.alumni.domain.entity.sector.Sector;

//tblfirma
@SuppressWarnings("serial")
@Entity
@Table(name = "company")
public class Company extends AbstractEntity {

	@Id
	private Long id;
	
	@Column(name = "company_name")
	private String company;
	
	@Column(name = "size")
	private String size;
	
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "sector", nullable = true)
	private Sector sector;
	
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy="department")
	private List<Department> departments;
	
	@Override
	public Long getId() {
		return id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<Department> getDepartments() {
		if(departments == null){
			departments = new ArrayList<Department>();
		}
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	
	public String toString(){
		return company;
	}

}
