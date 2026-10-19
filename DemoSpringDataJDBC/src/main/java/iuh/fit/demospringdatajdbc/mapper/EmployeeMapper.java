/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatajdbc.mapper;

import iuh.fit.demospringdatajdbc.dto.response.EmployeeResponse;
import iuh.fit.demospringdatajdbc.entity.Department;
import iuh.fit.demospringdatajdbc.entity.Employee;
import iuh.fit.demospringdatajdbc.entity.Position;
import iuh.fit.demospringdatajdbc.repository.DepartmentRepository;
import iuh.fit.demospringdatajdbc.repository.PositionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapper  {
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public EmployeeResponse toResponse(Employee employee) {
        return toResponses(List.of(employee)).get(0);
    }

    public List<EmployeeResponse> toResponses(List<Employee> employees) {
        Map<Integer, String> departmentNames = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));
        Map<Integer, String> positionTitles = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getTitle));

        return employees.stream()
                .map(e -> EmployeeResponse.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .role(e.getRole())
                        .age(e.getAge())
                        .salary(e.getSalary())
                        .departmentId(e.getDepartmentId())
                        .departmentName(departmentNames.get(e.getDepartmentId()))
                        .positionId(e.getPositionId())
                        .positionTitle(positionTitles.get(e.getPositionId()))
                        .build())
                .toList();
    }
}
