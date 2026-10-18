/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringjdbc.repository;

import iuh.fit.demospringjdbc.dto.request.SearchEmployeeRequest;
import iuh.fit.demospringjdbc.dto.response.EmployeeResponse;
import iuh.fit.demospringjdbc.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {
    private static final String BASE_SELECT = """
            SELECT e.id, e.name, e.role, e.age, e.salary, e.department_id, e.position_id, 
                d.name AS department_name, p.title AS position_title 
            FROM employees e 
            LEFT JOIN departments d ON e.department_id = d.id 
            LEFT JOIN positions p ON e.position_id = p.id
            """;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<EmployeeResponse> rowMapper = (rs, rowNum) -> EmployeeResponse.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .role(rs.getString("role"))
            .age(rs.getObject("age", Integer.class))
            .salary(rs.getObject("salary", Double.class))
            .departmentId(rs.getObject("department_id", Integer.class))
            .departmentName(rs.getString("department_name"))
            .positionId(rs.getObject("position_id", Integer.class))
            .positionTitle(rs.getString("position_title"))
            .build();

    public List<EmployeeResponse> findAll() {
        return jdbcTemplate.query(BASE_SELECT + " ORDER BY e.id", rowMapper);
    }

    public Optional<EmployeeResponse> findById(Integer id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(BASE_SELECT + " WHERE e.id  = ?", rowMapper, id)
            );
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public EmployeeResponse save(Employee e) {
        return e.getId() == null ? insert(e) : update(e);
    }

    private EmployeeResponse insert(Employee e) {
        String sql = """
                INSERT INTO employees(name, role, age, salary, department_id, position_id) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setObject(3, e.getAge());
            ps.setObject(4, e.getSalary());
            ps.setObject(5, e.getDepartmentId());
            ps.setObject(6, e.getPositionId());
            return ps;
        }, keyHolder);

        Integer newId = keyHolder.getKey().intValue();
        return findById(newId).orElseThrow();
    }

    private EmployeeResponse update(Employee e) {
        String sql = """
                UPDATE employees 
                SET name = ?, role = ?, age = ?, salary = ?, department_id = ?, position_id = ? 
                WHERE id = ?
                """;
        int rows = jdbcTemplate.update(sql, e.getName(), e.getRole(), e.getAge(), e.getSalary(),
                e.getDepartmentId(), e.getPositionId(), e.getDepartmentId());
        if (rows == 0)
            throw new EmptyResultDataAccessException("Không tìm thấy nhân viên với id=" + e.getId(), 1);
        return findById(e.getId()).orElseThrow();
    }

    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM employees WHERE id = ?", id);
    }

    public List<EmployeeResponse> findByNameContaining(String name) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE LOWER(e.name) LIKE ? ORDER BY e.id",
                rowMapper, "%" + name.trim().toLowerCase() + "%"
        );
    }

    public List<EmployeeResponse> findByAgeBetween(int from, int to) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE e.age BETWEEN ? AND ? ORDER BY e.age, e.id",
                rowMapper, from, to
        );
    }

    public List<EmployeeResponse> findBySalaryGreaterThan(Double salary) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE e.salary > ? ORDER BY e.salary DESC",
                rowMapper, salary
        );
    }

    public List<EmployeeResponse> search(SearchEmployeeRequest request) {
        StringBuilder sql = new StringBuilder(BASE_SELECT).append(" WHERE 1=1");
        List<Object> args = new ArrayList<>();

        if (request.getId() != null) {
            sql.append(" AND e.id = ?");
            args.add(request.getId());
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            sql.append(" AND LOWER(e.name) LIKE ?");
            args.add("%" + request.getName().trim().toLowerCase() + "%");
        }
        if (request.getMinAge() != null) {
            sql.append(" AND e.age >= ?");
            args.add(request.getMinAge());
        }
        if (request.getMaxAge() != null) {
            sql.append(" AND e.age <= ?");
            args.add(request.getMaxAge());
        }
        if (request.getMinSalary() != null) {
            sql.append(" AND e.salary > ?");
            args.add(request.getMinSalary());
        }
        if (request.getPositionId() != null) {
            sql.append(" AND e.position_id = ?");
            args.add(request.getPositionId());
        }
        sql.append(" ORDER BY e.id");
        return jdbcTemplate.query(sql.toString(), rowMapper, args.toArray());
    }

    public List<EmployeeResponse> findByPositionId(Integer positionId) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE e.position_id = ? ORDER BY e.id",
                rowMapper, positionId
        );
    }

    public List<EmployeeResponse> findByDepartmentIdAndPositionId(Integer departmentId, Integer positionId) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE e.department_id = ? AND e.position_id = ? ORDER BY e.id",
                rowMapper, departmentId, positionId
        );
    }

    public List<EmployeeResponse> findTopByDepartmentIdOrderBySalaryDesc(Integer departmentId, int n) {
        return jdbcTemplate.query(
                BASE_SELECT + " WHERE e.department_id = ? ORDER BY e.salary DESC LIMIT ?",
                rowMapper, departmentId, n
        );
    }
}
