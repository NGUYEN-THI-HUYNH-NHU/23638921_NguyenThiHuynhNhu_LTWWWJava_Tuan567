/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/20/2026
 */

package iuh.fit.demospringdatamongodb.service;

import iuh.fit.demospringdatamongodb.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringdatamongodb.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringdatamongodb.entity.Department;
import iuh.fit.demospringdatamongodb.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public List<DepartmentEmployeeCountResponse> countEmployeesByDepartment() {
        return departmentRepository.countEmployeesByDepartment();
    }

    public List<DepartmentSalaryResponse> salaryStatsByDepartment() {
        return departmentRepository.salaryStatsByDepartment();
    }

    public List<Department> findWithoutEmployees() {
        return departmentRepository.findWithoutEmployees();
    }
}
