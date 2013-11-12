package ro.scutaru.trainingjpa.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class DepartmentDaoTest {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private UserTransaction utx;

	@Inject
	private DepartmentDao dao;

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
	public void shouldFindADepartmentByName() throws Exception {
		insertDepartments("IT","HR");

		Department dept = dao.findDepartmentByName("IT");

		assertNotNull(dept);
	}

	// ------------------------------------------------------------

	private void insertDepartments(String...deptNames) throws Exception {
		utx.begin();
		em.joinTransaction();
		
		for (String deptName:deptNames) {
			em.persist(createDepartment(deptName));
		}
		utx.commit();
	}

	private Department createDepartment(String deptName) {
		Department d = new Department();
		d.setName(deptName);
		return d;
	}

}
