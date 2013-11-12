package ro.scutaru.trainingjpa.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ro.scutaru.trainingjpa.domain.Employee;

@Stateless
public class EmployeeDaoImpl implements EmployeeDao{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void save(Employee e){
		em.persist(e);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public List<Employee> getAllEmployees() {
		return em.createNamedQuery("allEmployees", Employee.class).getResultList();
	}

}
