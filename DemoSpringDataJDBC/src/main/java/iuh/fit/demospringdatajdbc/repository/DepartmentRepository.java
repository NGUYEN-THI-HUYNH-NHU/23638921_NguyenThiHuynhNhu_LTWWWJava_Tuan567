/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajdbc.repository;

import iuh.fit.demospringdatajdbc.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringdatajdbc.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringdatajdbc.entity.Department;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DepartmentRepository extends ListCrudRepository<Department, Integer> {
    @Query("""
            SELECT d.id, d.name
            FROM departments d LEFT JOIN employees e ON e.department_id = d.id
            WHERE e.id IS NULL
            ORDER BY d.id
            """)
    List<Department> findWithoutEmployees();

    @Query("""
            SELECT d.id, d.name, COUNT(e.id) AS employee_count
            FROM departments d LEFT JOIN employees e ON e.department_id = d.id
            GROUP BY d.id, d.name
            ORDER BY d.id
            """)
    List<DepartmentEmployeeCountResponse> countEmployeesByDepartment();

    @Query("""
            SELECT d.id, d.name,
                COALESCE(AVG(e.salary), 0) AS avg_salary,
                COALESCE(SUM(e.salary), 0) AS total_salary
            FROM departments d LEFT JOIN employees e ON e.department_id = d.id
            GROUP BY d.id, d.name
            ORDER BY d.id
            """)
    List<DepartmentSalaryResponse> salaryStatsByDepartment();
}
