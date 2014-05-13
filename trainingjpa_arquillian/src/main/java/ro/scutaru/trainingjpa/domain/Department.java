package ro.scutaru.trainingjpa.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

@NamedQueries({
	@NamedQuery(name="deptByName", query="select d from Department d where d.name=:name"), 
	@NamedQuery(name="deptByEmployeeName", query ="select d from Department d where d.id in (select e.dept.id from Employee e where e.firstName=:name)"),
	@NamedQuery(name="deptByEmployeeName2", query ="select d from Department d where exists (select e.dept from Employee e where e.firstName=:name and e.dept=d)"),
	@NamedQuery(name="deptByNameWithFetch", query="select distinct d from Department d join fetch d.employees where d.name =:name"), 
	@NamedQuery(name="deptWhoseNamesStartWith", query="select d from Department d where d.name LIKE CONCAT(:name, '%')")
})
@Entity
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;

	private String name;

	@OneToMany(cascade={CascadeType.REMOVE}, fetch=FetchType.LAZY, mappedBy="dept")
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
	
	@PrePersist
	private void testPrePersist(){
		//System.out.println(">>>>>>>>>>>>>>>>>> PrePersist called. ID="+id);
	}

	@PostPersist
	private void testPostPersist(){
		//fails because of this: https://bugs.eclipse.org/bugs/show_bug.cgi?id=363519
//		System.out.println(">>>>>>>>>>>>>>>>>> PostPersist called. ID="+id);
	}

}
