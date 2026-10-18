/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringjdbc.repository;

import iuh.fit.demospringjdbc.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringjdbc.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringjdbc.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Department> rowMapper = (rs, i) -> Department.builder()
            .id(rs.getInt("id")).name(rs.getString("name"))
            .build();

    public List<Department> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM departments ORDER BY id", rowMapper);
    }

    public List<Department> findWithoutEmployees() {
        String sql = """
                SELECT d.id, d.name
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id 
                WHERE e.id IS NULL
                ORDER BY d.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<DepartmentEmployeeCountResponse> countEmployeesByDepartment() {
        String sql = """
                SELECT d.id, d.name, COUNT(e.id) AS employee_count 
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id 
                GROUP BY d.id, d.name 
                ORDER BY d.id
                """;
        return jdbcTemplate.query(sql, (rs, i) -> DepartmentEmployeeCountResponse.builder()
                .departmentId(rs.getInt("id"))
                .departmentName(rs.getString("name"))
                .employeeCount(rs.getLong("employee_count"))
                .build());
    }

    public List<DepartmentSalaryResponse> salaryStatsByDepartment() {
        String sql = """
                SELECT d.id, d.name, 
                    COALESCE(AVG(e.salary), 0) AS avg_salary, 
                    COALESCE(SUM(e.salary), 0) AS total_salary
                FROM departments d LEFT JOIN employees e ON e.department_id = d.id 
                GROUP BY d.id, d.name 
                ORDER BY d.id
                """;
        return jdbcTemplate.query(sql, (rs, i) -> DepartmentSalaryResponse.builder()
                .departmentId(rs.getInt("id"))
                .departmentName(rs.getString("name"))
                .avgSalary(rs.getDouble("avg_salary"))
                .totalSalary(rs.getDouble("total_salary"))
                .build());
    }
}
