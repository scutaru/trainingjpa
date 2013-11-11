package ro.scutaru.trainingjpa.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Regular")
public class RegularEmployee extends Employee{
	
	public RegularEmployee() {
		super();
	}

	public RegularEmployee(String firstName, char middleInitial,
			String lastName, String streetAddress, String city, String country,
			double salary) {
		super(firstName, middleInitial, lastName, streetAddress, city, country);
		this.salary = salary;
	}

	private double salary;

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
}
