/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatajpa.mapper;

import iuh.fit.demospringdatajpa.dto.response.EmployeeResponse;
import iuh.fit.demospringdatajpa.entity.Department;
import iuh.fit.demospringdatajpa.entity.Employee;
import iuh.fit.demospringdatajpa.entity.Position;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeMapper {
    public EmployeeResponse toResponse(Employee e) {
        Department d = e.getDepartment();
        Position p = e.getPosition();
        return EmployeeResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .age(e.getAge())
                .salary(e.getSalary())
                .departmentId(d != null ? d.getId() : null)
                .departmentName(d != null ? d.getName() : null)
                .positionId(p != null ? p.getId() : null)
                .positionTitle(p != null ? p.getTitle() : null)
                .build();
    }

    public List<EmployeeResponse> toResponses(List<Employee> employees) {
        return employees.stream().map(this::toResponse).toList();
    }
}
