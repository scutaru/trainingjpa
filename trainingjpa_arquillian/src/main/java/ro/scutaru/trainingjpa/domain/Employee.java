package ro.scutaru.trainingjpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

@Entity
@NamedQueries({ @NamedQuery(name = "allEmployees", query = "select e from Employee e order by e.id") })
public class Employee {
	@SequenceGenerator(name="Seq_Gen", sequenceName="Seq_Gen")
	@Id @GeneratedValue(generator="Seq_Gen", strategy=GenerationType.SEQUENCE) 
	private int id;
	private String firstName;
	private String lastName;
	
	@JoinColumn(name="dept_fk")
	private Department dept;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

}
