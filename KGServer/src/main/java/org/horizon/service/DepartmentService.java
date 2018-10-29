package org.horizon.service;

import org.horizon.bean.Department;
import org.horizon.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sang on 2018/1/7.
 */
@Service
@Transactional
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;
    public int addDep(Department department) {
        department.setEnabled(true);
        departmentMapper.addDep(department);
        return department.getResult();
    }

    public int deleteDep(Long did) {
        Department department = new Department();
        department.setId(did);
        departmentMapper.deleteDep(department);
        return department.getResult();
    }

    public List<Department> getDepByPid(Long pid) {
        return departmentMapper.getDepByPid(pid);
    }

    public List<Department> getAllDeps() {
        return departmentMapper.getAllDeps();
    }
}
