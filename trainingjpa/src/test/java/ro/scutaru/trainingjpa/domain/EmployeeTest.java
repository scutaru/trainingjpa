package ro.scutaru.trainingjpa.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class EmployeeTest extends EntityTest{
	@Test
	public void insertAndRetrieve() {
		final Employee e1 = new Employee("Ionut", 'V', "Scutaru", "Strada",
				"Bucuresti", "Romania");
		final Employee e2 = new Employee("Ionut2", 'V', "Scutaru2", "Strada",
				"Bucuresti", "Romania");
		
		em.getTransaction().begin();
		em.persist(e1);
		em.persist(e2);
		em.getTransaction().commit();

		final List<Employee> list = em.createQuery("select e from Employee e",
				Employee.class).getResultList();

		assertEquals(2, list.size());
		for (Employee current : list) {
			final String firstName = current.getFirstName();
			assertTrue(firstName.equals("Ionut") || firstName.equals("Ionut2"));
		}
	}
	
	@Test
	public void insertAndRetrieveWithDept(){
		Employee e = new Employee("Ionut", 'V', "Scutaru", "Strada",
				"Bucuresti", "Romania");
		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees.add(e);
		Department dept = new Department("IT", employees);
		e.setDept(dept);
		
		em.getTransaction().begin();
		em.persist(e);
		em.persist(dept);
		em.getTransaction().commit();
		
		List<Employee> resultList = em.createQuery("SELECT e FROM Employee e WHERE e.dept.name='IT'", Employee.class).getResultList();
		assertEquals(1, resultList.size());
	}
}