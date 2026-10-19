package iuh.fit.demospringdatajpa.repository;

import iuh.fit.demospringdatajpa.dto.response.DepartmentEmployeeCountResponse;
import iuh.fit.demospringdatajpa.dto.response.DepartmentSalaryResponse;
import iuh.fit.demospringdatajpa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("""
            SELECT d FROM Department d
            WHERE NOT EXISTS (SELECT 1 FROM Employee e WHERE e.department = d)
            ORDER BY d.id
            """)
    List<Department> findWithoutEmployees();

    @Query("""
            SELECT new iuh.fit.demospringdatajpa.dto.response.DepartmentEmployeeCountResponse(
                       d.id, d.name, COUNT(e))
            FROM Department d
            LEFT JOIN Employee e ON e.department = d
            GROUP BY d.id, d.name
            ORDER BY d.id
            """)
    List<DepartmentEmployeeCountResponse> countEmployeesByDepartment();

    @Query("""
            SELECT new iuh.fit.demospringdatajpa.dto.response.DepartmentSalaryResponse(
                       d.id, d.name, CAST(COALESCE(AVG(e.salary), 0.0) AS DOUBLE), CAST(COALESCE(SUM(e.salary), 0.0) AS DOUBLE))
            FROM Department d
            LEFT JOIN Employee e ON e.department = d
            GROUP BY d.id, d.name
            ORDER BY d.id
            """)
    List<DepartmentSalaryResponse> salaryStatsByDepartment();
}
