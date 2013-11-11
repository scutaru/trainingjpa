package ro.scutaru.trainingjpa.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Test;

public class EmployeeInheritanceTest extends EntityTest{
	private static final String TESTER_ET_COMP = "Tester et comp.";

	@Test
	public void insertAndRetrieve(){
		RegularEmployee re = new RegularEmployee("Regular", 'T', "Tester", "Testing Street", "TestCity", "TestLand", 10d);
		ContractorEmployee ce = new ContractorEmployee("Contractor", 'T', "Tester", "Testing Street", "TestCity", "TestLand", TESTER_ET_COMP, 300);
		
		em.getTransaction().begin();
		em.persist(ce);
		em.persist(re);
		em.getTransaction().commit();
		
		List<ContractorEmployee> empFound = findContractorEmployeeByCompanyName(TESTER_ET_COMP);
		assertEquals(1, empFound.size());
	}

	private List<ContractorEmployee> findContractorEmployeeByCompanyName(String companyName) {
		TypedQuery<ContractorEmployee> query = em.createQuery("from "+ContractorEmployee.class.getName()+" re where re.company=?1", ContractorEmployee.class);
		query.setParameter(1, companyName);
		return query.getResultList();
	}
	
}
