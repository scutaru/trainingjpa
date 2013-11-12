package ro.scutaru.trainingjpa.dao;

import java.util.List;

import ro.scutaru.trainingjpa.domain.Employee;

public interface EmployeeDao {

	void save(Employee e);

	List<Employee> getAllEmployees();

}
