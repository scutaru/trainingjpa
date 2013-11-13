package ro.scutaru.trainingjpa.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.PersistenceTest;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.Transactional;
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

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackage(Employee.class.getPackage())
				.addPackage(EmployeeDao.class.getPackage())
				.addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@UsingDataSet({ "datasets/departments.xml","datasets/employees.xml" })
	//@Transactional(TransactionMode.DISABLED)
	public void shouldFindADepartmentByName() throws Exception {
		Department dept = dao.findDepartmentByName("IT");

		assertNotNull(dept);
		assertEquals("IT", dept.getName());
		assertEquals(2, dept.getEmployees().size());
	}

}
