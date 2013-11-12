package ro.scutaru.trainingjpa.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
	@NamedQuery(name="deptByName", query="select d from Department d where d.name=:name")
})
@Entity
public class Department {

	@Id
	@GeneratedValue
	private int id;

	private String name;

	@OneToMany
	private Collection<Employee> employees;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof Department)) {
			return false;
		}
		Department other = (Department) o;
		if (name.equals(other.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public  int hashCode(){
		return name.hashCode();
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}
}
