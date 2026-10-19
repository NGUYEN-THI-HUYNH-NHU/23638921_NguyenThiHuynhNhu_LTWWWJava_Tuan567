/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajdbc.repository;

import iuh.fit.demospringdatajdbc.entity.Employee;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends ListCrudRepository<Employee, Integer> {
    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByAgeBetween(Integer from, Integer to);

    List<Employee> findBySalaryGreaterThan(Double salary);

    List<Employee> findByPositionId(Integer positionId);

    List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);

    @Query("""
            SELECT * FROM employees
            WHERE department_id = :departmentId
            ORDER BY salary DESC
            LIMIT :n
            """)
    List<Employee> findTopNByDepartmentId(@Param("departmentId") Integer departmentId,
                                          @Param("n") int n);

    @Query("""
            SELECT * FROM employees
            WHERE (:id IS NULL OR id = :id)
                AND (:name IS NULL OR LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')))
                AND (:minAge IS NULL OR age >= :minAge)
                AND (:maxAge IS NULL OR age <= :maxAge)
                AND (:minSalary IS NULL OR salary > :minSalary)
                AND (:positionId IS NULL OR position_id = :positionId)
            ORDER BY id
            """)
    List<Employee> search(@Param("id") Integer id,
                          @Param("name") String name,
                          @Param("minAge") Integer minAge,
                          @Param("maxAge") Integer maxAge,
                          @Param("minSalary") Double minSalary,
                          @Param("positionId") Integer positionId);
}
