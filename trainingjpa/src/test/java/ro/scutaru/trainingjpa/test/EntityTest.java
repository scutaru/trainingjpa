package ro.scutaru.trainingjpa.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

public class EntityTest {
	protected EntityManagerFactory emf;
	protected EntityManager em;

	@Before
	public void initEntityManager() {
		emf = Persistence.createEntityManagerFactory("trainingJPA");
		em = emf.createEntityManager();
	}

	@After
	public void cleanup() {
		em.close();
		emf.close();
	}
}
