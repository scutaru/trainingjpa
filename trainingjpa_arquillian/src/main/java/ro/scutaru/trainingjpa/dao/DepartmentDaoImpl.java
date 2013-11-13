package ro.scutaru.trainingjpa.dao;

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
}
