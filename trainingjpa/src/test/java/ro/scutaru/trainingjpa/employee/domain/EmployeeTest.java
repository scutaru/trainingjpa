package ro.scutaru.trainingjpa.employee.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ro.scutaru.trainingjpa.employee.domain.Department;
import ro.scutaru.trainingjpa.employee.domain.Employee;
import ro.scutaru.trainingjpa.test.EntityTest;

public class EmployeeTest extends EntityTest {
	Date birthdate = new Date(System.currentTimeMillis());

	@Test
	public void insertAndRetrieve() {
		final Employee e1 = new Employee("Ionut", 'V', "Scutaru", "Strada",
				"Bucuresti", "Romania", birthdate);
		final Employee e2 = new Employee("Ionut2", 'V', "Scutaru2", "Strada",
				"Bucuresti", "Romania", birthdate);

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
			assertEquals(birthdate, current.getBirthdate());
		}
	}

	@Test
	public void insertAndRetrieveWithDept() {
		Employee e = createEmployee(1);
		ArrayList<Employee> employees = new ArrayList<Employee>();
		employees.add(e);
		Department dept = new Department("IT", employees);
		e.setDept(dept);

		em.getTransaction().begin();
		em.persist(e);
		em.persist(dept);
		em.getTransaction().commit();

		List<Employee> resultList = em.createQuery(
				"SELECT e FROM Employee e WHERE e.dept.name='IT'",
				Employee.class).getResultList();
		assertEquals(1, resultList.size());
	}

	@Test
	public void insertAndRetrieveWithDeptUsingJoin() {
		Department it = new Department("IT");
		Department hr = new Department("HR");
		List<Employee> employeesIT = createEmployeesForDept(2, it);
		List<Employee> employeesHR = createEmployeesForDept(3, hr);

		em.getTransaction().begin();
		em.persist(it);
		em.persist(hr);
		for (Employee e : employeesIT) {
			em.persist(e);
		}
		for (Employee e : employeesHR) {
			em.persist(e);
		}
		em.getTransaction().commit();

		List<Employee> resultList = em.createQuery(
				"SELECT e FROM Employee e JOIN e.dept d WHERE d.name='IT'",
				Employee.class).getResultList();
		assertEquals(2, resultList.size());
	}

	@Test
	public void exampleProjection() {
		Department it = new Department("IT");
		List<Employee> employeesIT = createEmployeesForDept(2, it);

		em.getTransaction().begin();
		em.persist(it);
		for (Employee e : employeesIT) {
			em.persist(e);
		}
		em.getTransaction().commit();

		List<?> resultList = em.createQuery(
				"SELECT e.lastName, e.firstName FROM Employee e")
				.getResultList();
		assertEquals(2, resultList.size());

		for (Object data : resultList) {
			Object[] fields = (Object[]) data;
			assertTrue(((String) fields[1]).contains("Ionut"));
		}
	}

	@Test
	public void exampleProjectionInstantiatingWithNew() {
		Department it = new Department("IT");
		List<Employee> employeesIT = createEmployeesForDept(2, it);

		em.getTransaction().begin();
		em.persist(it);
		for (Employee e : employeesIT) {
			em.persist(e);
		}
		em.getTransaction().commit();

		List<?> names = em.createQuery(
				"SELECT NEW " + Name.class.getName()
						+ "(e.firstName, e.lastName) FROM Employee e")
				.getResultList();
		assertEquals(2, names.size());

		for (Object data : names) {
			Name name = (Name) data;
			assertTrue(name.getFirstName().contains("Ionut"));
		}
	}

	private List<Employee> createEmployeesForDept(int nbEmployees,
			Department dept) {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for (int i = 0; i < nbEmployees; i++) {
			Employee e = createEmployee(1);
			e.setDept(dept);
			employees.add(e);
		}
		return employees;
	}

	private Employee createEmployee(int id) {
		Employee e = new Employee("Ionut" + id, 'V', "Scutaru" + id, "Strada",
				"Bucuresti", "Romania", birthdate);
		return e;
	}

}