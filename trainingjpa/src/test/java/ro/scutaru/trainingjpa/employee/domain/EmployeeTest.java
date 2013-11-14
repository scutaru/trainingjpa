package ro.scutaru.trainingjpa.employee.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Test;

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
		persistDepartments(it, hr);
		persistEmployees(union(employeesIT, employeesHR));
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
			assertTrue(((String) fields[1]).contains("John"));
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
			assertTrue(name.getFirstName().contains("John"));
		}
	}

	@Test
	public void exampleFetchJoin() {
		Department it = new Department("IT");
		List<Employee> employees = createEmployeesForDept(2, it);

		em.getTransaction().begin();
		em.persist(it);
		for (Employee e : employees) {
			em.persist(e);
		}
		it.setEmployees(employees);
		em.getTransaction().commit();

		TypedQuery<Department> query = em.createQuery(
				"SELECT DISTINCT d FROM Department d JOIN FETCH d.employees",
				Department.class);

		List<Department> departments = query.getResultList();
		assertEquals(1, departments.size());
		assertEquals(2, departments.iterator().next().getEmployees().size());
	}

	@Test
	public void exampleLike() {
		Department it = new Department("IT");
		List<Employee> employees = createEmployeesForDept(2, it);

		em.getTransaction().begin();
		em.persist(it);
		for (Employee e : employees) {
			em.persist(e);
		}
		it.setEmployees(employees);
		em.getTransaction().commit();

		TypedQuery<Employee> query = em.createQuery(
				"SELECT e FROM Employee e WHERE e.firstName LIKE '%2'",
				Employee.class);

		List<Employee> foundEmployees = query.getResultList();
		assertEquals(1, foundEmployees.size());
	}

	@Test
	public void exampleSubquery() {
		Department it = new Department("IT");
		Department hr = new Department("HR");
		List<Employee> employeesIT = createEmployeesForDept(2, it);
		List<Employee> employeesHR = createEmployeesForDept(5, hr);

		em.getTransaction().begin();
		persistDepartments(it, hr);
		persistEmployees(union(employeesIT, employeesHR));
		it.setEmployees(employeesIT);
		hr.setEmployees(employeesHR);
		em.getTransaction().commit();

		List<Employee> foundEmployees = em
				.createQuery(
						"SELECT e FROM Employee e WHERE e.dept IN (SELECT d FROM Department d WHERE d.name='HR')",
						Employee.class).getResultList();

		assertEquals(5, foundEmployees.size());
	}

	private List<Employee> union(List<Employee> employeesIT,
			List<Employee> employeesHR) {
		List<Employee> employees = new ArrayList<Employee>(employeesIT.size()
				+ employeesHR.size());
		employees.addAll(employeesIT);
		employees.addAll(employeesHR);
		return employees;
	}

	@Test
	public void exampleDelete() {
		Department it = new Department("IT");
		List<Employee> employeesIT = createEmployeesForDept(5, it);

		em.getTransaction().begin();
		persistDepartments(it);
		persistEmployees(employeesIT);
		it.setEmployees(employeesIT);
		em.getTransaction().commit();

		em.getTransaction().begin();
		em.createQuery("DELETE FROM Employee e WHERE e.lastName ='Doe1'")
				.executeUpdate();
		em.getTransaction().commit();

		long remainingEmployees = (Long)em.createQuery("SELECT COUNT(e) FROM Employee e").getSingleResult();
		
		assertEquals(4, remainingEmployees);
	}

	@Test
	public void exampleUpdate() {
		Department it = new Department("IT");
		List<Employee> employeesIT = createEmployeesForDept(5, it);

		em.getTransaction().begin();
		persistDepartments(it);
		persistEmployees(employeesIT);
		it.setEmployees(employeesIT);
		em.getTransaction().commit();

		em.getTransaction().begin();
		em.createQuery("UPDATE Employee e SET e.lastName ='Travolta' WHERE e.lastName='Doe1'")
				.executeUpdate();
		em.getTransaction().commit();

		long remainingEmployees = (Long)em.createQuery("SELECT COUNT(e) FROM Employee e where e.lastName='Travolta'").getSingleResult();
		
		assertEquals(1, remainingEmployees);
	}

	// ----------------------------------------------------------------------
	private void persistEmployees(List<Employee> listsOfEmployees) {
		for (Employee e : listsOfEmployees) {
			em.persist(e);
		}
	}

	private void persistDepartments(Department... departments) {
		for (Department d : departments) {
			em.persist(d);
		}
	}

	private List<Employee> createEmployeesForDept(int nbEmployees,
			Department dept) {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for (int i = 0; i < nbEmployees; i++) {
			Employee e = createEmployee(i + 1);
			e.setDept(dept);
			employees.add(e);
		}
		return employees;
	}

	private Employee createEmployee(int id) {
		return new Employee("John" + id, 'D', "Doe" + id, "Street",
				"Bucuresti", "Romania", birthdate);
	}

}