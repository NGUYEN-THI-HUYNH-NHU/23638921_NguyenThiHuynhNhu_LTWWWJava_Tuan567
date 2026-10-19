/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajdbc.service;

import iuh.fit.demospringdatajdbc.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringdatajdbc.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringdatajdbc.entity.Department;
import iuh.fit.demospringdatajdbc.repository.DepartmentRepository;
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

    public List<Department> findWithoutEmployees() {
        return departmentRepository.findWithoutEmployees();
    }

    public List<DepartmentEmployeeCountResponse> countEmployeesByDepartment() {
        return departmentRepository.countEmployeesByDepartment();
    }

    public List<DepartmentSalaryResponse> salaryStatsByDepartment() {
        return departmentRepository.salaryStatsByDepartment();
    }
}
