package ro.scutaru.trainingjpa.dao;

import ro.scutaru.trainingjpa.domain.Department;

public interface DepartmentDao {

	Department findDepartmentByName(String deptName);

}
