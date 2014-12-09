package ro.scutaru.trainingjpa.employee.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import ro.scutaru.trainingjpa.test.EntityTest;

public class EmployeeInheritanceTest extends EntityTest{
	private static final String TESTER_ET_COMP = "Tester et comp.";

	@Test
	public void insertAndRetrieve(){
		RegularEmployee re = createRegularEmployee(1);
		ContractorEmployee ce = new ContractorEmployee("Contractor", 'T', "Tester", "Testing Street", "TestCity", "TestLand", TESTER_ET_COMP, 300, null);
		
		em.getTransaction().begin();
		em.persist(ce);
		em.persist(re);
		em.getTransaction().commit();
		
		List<ContractorEmployee> empFound = findContractorEmployeeByCompanyName(TESTER_ET_COMP);
		assertEquals(1, empFound.size());
	}
	
	@Test
	public void exampleSelectingRegularEmployeesUsingTypeKeyword(){
		List<RegularEmployee> regularEmployees = createRegularEmployees(2);
		List<ContractorEmployee> contractorEmployees = createContractorEmployees(3);

		em.getTransaction().begin();
		for(RegularEmployee e:regularEmployees){
			em.persist(e);
		}
		for(ContractorEmployee e:contractorEmployees){
			em.persist(e);
		}
		em.getTransaction().commit();
		
		Query query = em.createQuery("SELECT e FROM Employee e WHERE TYPE(e)<>RegularEmployee");
		@SuppressWarnings("unchecked")
		List<RegularEmployee> contractorEmployeesReturned = query.getResultList();
		assertEquals(contractorEmployees.size(), contractorEmployeesReturned.size());
	}

	//--------------------------------------------------------------------------------
	
	private List<RegularEmployee> createRegularEmployees(int nbEmployees) {
		List<RegularEmployee> regularEmployees = new ArrayList<RegularEmployee>();
		for(int i=0;i<nbEmployees ;i++){
			RegularEmployee e = createRegularEmployee(i);
			regularEmployees.add(e);
		}
		return regularEmployees;
	}

	private List<ContractorEmployee> createContractorEmployees(int nbEmployees) {
		List<ContractorEmployee> contractorEmployees = new ArrayList<ContractorEmployee>();
		for(int i=0;i<nbEmployees ;i++){
			ContractorEmployee e = createContractorEmployee(i);
			contractorEmployees.add(e);
		}
		return contractorEmployees;
	}

	private RegularEmployee createRegularEmployee(int id) {
		return new RegularEmployee("Regular"+id, 'T', "Tester"+id, "Testing Street", "TestCity", "TestLand", 10d, null);
	}

	private ContractorEmployee createContractorEmployee(int id) {
		return new ContractorEmployee("Contractor", 'T', "Tester", "Testing Street", "TestCity", "TestLand", TESTER_ET_COMP, 300, null);
	}

	private List<ContractorEmployee> findContractorEmployeeByCompanyName(String companyName) {
		TypedQuery<ContractorEmployee> query = em.createQuery("from "+ContractorEmployee.class.getName()+" re where re.company=?1", ContractorEmployee.class);
		query.setParameter(1, companyName);
		return query.getResultList();
	}
	
}
