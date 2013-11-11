package ro.scutaru.trainingjpa.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Department {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="dept")
	private Collection<Employee> employees;

	public Department(String name, Collection<Employee> employees) {
		super();
		this.name = name;
		this.employees = employees;
	}

	public Department() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}
}
