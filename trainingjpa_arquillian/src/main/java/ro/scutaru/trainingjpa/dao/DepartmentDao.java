package ro.scutaru.trainingjpa.dao;

import java.util.List;

import ro.scutaru.trainingjpa.domain.Department;

public interface DepartmentDao {

	Department findDepartmentByName(String deptName);

	List<Department> findDepartmentByEmployeeName(String employeeName);

	Department findDepartmentByNameUsingCriteria(String deptName);

	void createDepartment(String deptName);

	Department findDepartmentByNameWithFetch(String deptName);

	void deleteDepartment(int deptId);

	List<Department> findDepartmentsWhoseNamesStartWith(String deptName);

}
