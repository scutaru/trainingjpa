package ro.scutaru.trainingjpa.employee.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import ro.scutaru.trainingjpa.employee.domain.Department;
import ro.scutaru.trainingjpa.employee.domain.Employee;
import ro.scutaru.trainingjpa.test.EntityTest;

public class DepartmentTest extends EntityTest{

	@Test
	public void insertAndRetrieve(){
		Department d1 = new Department("IT", createITEmployees());
		em.getTransaction().begin();
		em.persist(d1);
		em.getTransaction().commit();
		List<Department> depts = getAllDepartments();
		assertEquals(1, depts.size());
		assertEquals(3, depts.iterator().next().getEmployees().size());
	}

	//-------------------------------------------------
	
	private List<Department> getAllDepartments() {
		return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
	}

	private Collection<Employee> createITEmployees() {
		Collection<Employee> employees = new ArrayList<Employee>();
		for(int i=0;i<3;i++){
			employees.add(createEmployee(i));
		}
		return employees;
	}

	private Employee createEmployee(int i) {
		Employee employee = new Employee();
		employee.setFirstName("FirstName"+i);
		employee.setFirstName("LastName"+i);
		employee.setMiddleInitial('A');
		employee.setStreetAddress("Street"+i);
		employee.setCity("City"+i);
		employee.setCountry("Country"+i);
		return employee;
	}
}
