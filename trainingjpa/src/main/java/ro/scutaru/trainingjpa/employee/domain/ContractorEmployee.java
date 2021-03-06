package ro.scutaru.trainingjpa.employee.domain;

import java.sql.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Contractor")
public class ContractorEmployee extends Employee{
	
	public ContractorEmployee() {
		super();
	}

	public ContractorEmployee(String firstName, char middleInitial,
			String lastName, String streetAddress, String city, String country,
			String company, double hourlyRate, Date birthdate) {
		super(firstName, middleInitial, lastName, streetAddress, city, country, birthdate);
		this.company = company;
		this.hourlyRate = hourlyRate;
	}

	private String company;
	private double hourlyRate;

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
