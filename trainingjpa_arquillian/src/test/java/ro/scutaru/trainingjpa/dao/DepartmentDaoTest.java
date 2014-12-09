package ro.scutaru.trainingjpa.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.scutaru.trainingjpa.domain.Department;
import ro.scutaru.trainingjpa.domain.Employee;

@RunWith(Arquillian.class)
@PersistenceTest
@Transactional(TransactionMode.ROLLBACK)
public class DepartmentDaoTest {
	@Inject
	private DepartmentDao dao;
	@Inject
	private EmployeeDao daoEmp;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(Employee.class.getPackage())
				.addPackage(EmployeeDao.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml", "datasets/employees.xml" })
	public void shouldFindADepartmentByName() throws Exception {
		Department dept = dao.findDepartmentByName("IT");

		assertNotNull(dept);
		assertEquals("IT", dept.getName());
		assertEquals(3, dept.getEmployees().size());
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml", "datasets/employees.xml" })
	public void shouldDeleteCascadeEmployees() throws Exception {
		dao.deleteDepartment(-2000);
		List<Department> deptsFound = dao.findDepartmentsWhoseNamesStartWith("IT");
		assertTrue(deptsFound.isEmpty());
		
		List<Employee> empList = daoEmp.getAllEmployees();
		assertTrue(empList.isEmpty());
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml", "datasets/employees.xml" })
	public void shouldFindADepartmentByNameWithFetch() throws Exception {
		Department dept = dao.findDepartmentByNameWithFetch("IT");

		assertNotNull(dept);
		assertEquals("IT", dept.getName());
		assertNotNull(dept.getEmployees());
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml", "datasets/employees.xml" })
	public void shouldFindDepartmentByEmployeeName() {
		List<Department> departments = dao
				.findDepartmentByEmployeeName("Bogdan");
		assertEquals(1, departments.size());
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml", "datasets/employees.xml" })
	public void shouldFindADepartmentByNameUsingCriteria() throws Exception {
		Department dept = dao.findDepartmentByNameUsingCriteria("IT");

		assertNotNull(dept);
		assertEquals("IT", dept.getName());
		assertEquals(3, dept.getEmployees().size());
	}

	@Test
	public void shouldPersistADepartment() {
		String deptName = "Accounting";
		dao.createDepartment(deptName);
		Department department = dao.findDepartmentByName(deptName);
		assertEquals(deptName, department.getName());
	}

}
