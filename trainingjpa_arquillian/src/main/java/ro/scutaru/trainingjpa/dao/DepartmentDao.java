package ro.scutaru.trainingjpa.dao;

import java.util.List;

import ro.scutaru.trainingjpa.domain.Department;

public interface DepartmentDao {

	Department findDepartmentByName(String deptName);

	List<Department> findDepartmentByEmployeeName(String employeeName);

	List<Department> findDepartmentByEmployeeName2(String employeeName);

}
