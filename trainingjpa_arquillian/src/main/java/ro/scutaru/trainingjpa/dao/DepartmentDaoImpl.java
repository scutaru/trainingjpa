package ro.scutaru.trainingjpa.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ro.scutaru.trainingjpa.domain.Department;

@Stateless
public class DepartmentDaoImpl implements DepartmentDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	//@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public Department findDepartmentByName(String deptName) {
		return em.createNamedQuery("deptByName", Department.class)
				.setParameter("name", deptName).getSingleResult();
	}
	
	@Override
	public List<Department> findDepartmentByEmployeeName(String employeeName){
		return em.createNamedQuery("deptByEmployeeName", Department.class)
				.setParameter("name", employeeName).getResultList();
	}

	@Override
	public List<Department> findDepartmentByEmployeeName2(String employeeName){
		return em.createNamedQuery("deptByEmployeeName2", Department.class)
				.setParameter("name", employeeName).getResultList();
	}

}
