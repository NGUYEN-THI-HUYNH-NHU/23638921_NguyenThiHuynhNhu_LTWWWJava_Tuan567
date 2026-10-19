package iuh.fit.demospringdatajpa.repository;

import iuh.fit.demospringdatajpa.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // ----- CRUD: nạp luôn department và position bằng 1 câu JOIN -----
    @Override
    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findAll();

    @Override
    @EntityGraph(attributePaths = {"department", "position"})
    Optional<Employee> findById(Integer id);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findByAgeBetween(Integer from, Integer to);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findBySalaryGreaterThan(Double salary);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findByPositionId(Integer positionId);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId);

    @EntityGraph(attributePaths = {"department", "position"})
    List<Employee> findByDepartmentIdOrderBySalaryDesc(Integer departmentId, Pageable pageable);

    @Query("""
            SELECT e FROM Employee e
            LEFT JOIN FETCH e.department
            LEFT JOIN FETCH e.position
            WHERE (:id IS NULL OR e.id = :id)
              AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:minAge IS NULL OR e.age >= :minAge)
              AND (:maxAge IS NULL OR e.age <= :maxAge)
              AND (:minSalary IS NULL OR e.salary > :minSalary)
              AND (:positionId IS NULL OR e.position.id = :positionId)
            ORDER BY e.id
            """)
    List<Employee> search(@Param("id") Integer id,
                          @Param("name") String name,
                          @Param("minAge") Integer minAge,
                          @Param("maxAge") Integer maxAge,
                          @Param("minSalary") Double minSalary,
                          @Param("positionId") Integer positionId);
}
