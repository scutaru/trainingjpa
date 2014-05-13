package ro.scutaru.trainingjpa.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ro.scutaru.trainingjpa.domain.Department;

@Stateless
public class DepartmentDaoImpl implements DepartmentDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Department findDepartmentByName(String deptName) {
		return em.createNamedQuery("deptByName", Department.class)
				.setParameter("name", deptName).getSingleResult();
	}

	@Override
	public List<Department> findDepartmentsWhoseNamesStartWith(String deptName) {
		return em.createNamedQuery("deptWhoseNamesStartWith", Department.class)
				.setParameter("name", deptName).getResultList();
	}
	
	@Override
	public void deleteDepartment(int deptId) {
		Department department = em.find(Department.class, deptId);
		em.remove(department);
	}
	
	
	@Override
	public List<Department> findDepartmentByEmployeeName(String employeeName){
		return em.createNamedQuery("deptByEmployeeName", Department.class)
				.setParameter("name", employeeName).getResultList();
	}

	@Override
	public Department findDepartmentByNameUsingCriteria(String deptName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Department> cq = cb.createQuery(Department.class);
		Root<Department> deptRoot = cq.from(Department.class);
		
		cq.select(deptRoot);
		cq.where(cb.equal(deptRoot.get("name"), deptName));
		
		TypedQuery<Department> createQuery = em.createQuery(cq);
		
		return createQuery.getSingleResult();
	}

	@Override
	public void createDepartment(String deptName) {
		Department dept = new Department();
		dept.setName(deptName);
		em.persist(dept);
	}
	
	@Override
	public Department findDepartmentByNameWithFetch(String deptName) {
		return em.createNamedQuery("deptByNameWithFetch", Department.class)
				.setParameter("name", deptName).getSingleResult();
	}
	

}
