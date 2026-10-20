/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/19/2026
 */

package iuh.fit.demospringdatamongodb.mapper;

import iuh.fit.demospringdatamongodb.dto.response.EmployeeResponse;
import iuh.fit.demospringdatamongodb.entity.Employee;
import iuh.fit.demospringdatamongodb.entity.EmployeeProjection;
import iuh.fit.demospringdatamongodb.entity.Position;
import iuh.fit.demospringdatamongodb.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final PositionRepository positionRepository;

    public EmployeeResponse toResponse(EmployeeProjection row) {
        return toResponses(List.of(row)).get(0);
    }

    public List<EmployeeResponse> toResponses(List<EmployeeProjection> rows) {
        Map<Integer, String> positionTitles = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getTitle));

        return rows.stream()
                .map(row -> {
                    Employee e = row.getEmployee();
                    return EmployeeResponse.builder()
                            .id(e.getEmployeeId())
                            .name(e.getName())
                            .role(e.getRole())
                            .age(e.getAge())
                            .salary(e.getSalary())
                            .departmentId(row.getDepartmentId())
                            .departmentName(row.getDepartmentName())
                            .positionId(e.getPositionId())
                            .positionTitle(positionTitles.get(e.getPositionId()))
                            .build();
                })
                .toList();
    }
}
