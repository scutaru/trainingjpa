package ro.scutaru.trainingjpa.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.scutaru.trainingjpa.domain.Department;
import ro.scutaru.trainingjpa.domain.Employee;

@RunWith(Arquillian.class)
public class EmployeeDaoTest {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private UserTransaction utx;

	@Inject
	private EmployeeDao dao;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackage(Employee.class.getPackage())
				.addPackage(EmployeeDao.class.getPackage())
				.addAsResource("test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void shouldFindAllGamesUsingJpqlQuery() throws Exception {
		insertEmployees(2);

		List<Employee> employees = dao.getAllEmployees();

		assertEquals(2, employees.size());
		assertSameDepartment(employees);
	}

	private void assertSameDepartment(List<Employee> employees) {
		Department lastDept = null;
		for(Employee e:employees){
			if(lastDept!=null && !lastDept.equals(e.getDept())){
				fail();
			}
		}
	}

	// ------------------------------------------------------------

	private void insertEmployees(int numberOfEmployees) throws Exception {
		Department d = createDepartment("IT");
		utx.begin();
		em.joinTransaction();
		em.persist(d);
		
		List<Employee> employees = new ArrayList<Employee>();
		for (int i = 0; i < numberOfEmployees; i++) {
			Employee e = createEmployeeObject(i);
			e.setDept(d);
			em.persist(e);
			employees.add(e);
		}
		d.setEmployees(employees);
		utx.commit();
	}

	private Department createDepartment(String deptName) {
		Department d = new Department();
		d.setName(deptName);
		return d;
	}

	private Employee createEmployeeObject(int id) {
		Employee e = new Employee();
		e.setFirstName("Test" + id);
		e.setLastName("Tester" + id);
		return e;
	}

}
