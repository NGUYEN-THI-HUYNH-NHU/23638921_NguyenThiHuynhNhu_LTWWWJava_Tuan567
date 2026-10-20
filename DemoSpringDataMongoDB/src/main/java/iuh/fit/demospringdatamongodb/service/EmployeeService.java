/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/20/2026
 */

package iuh.fit.demospringdatamongodb.service;

import iuh.fit.demospringdatamongodb.dto.request.SaveEmployeeRequest;
import iuh.fit.demospringdatamongodb.dto.request.SearchEmployeeRequest;
import iuh.fit.demospringdatamongodb.dto.response.EmployeeResponse;
import iuh.fit.demospringdatamongodb.entity.Employee;
import iuh.fit.demospringdatamongodb.mapper.EmployeeMapper;
import iuh.fit.demospringdatamongodb.repository.DepartmentRepository;
import iuh.fit.demospringdatamongodb.repository.EmployeeRepository;
import iuh.fit.demospringdatamongodb.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeResponse> findAll() {
        return employeeMapper.toResponses(employeeRepository.findAll());
    }

    public EmployeeResponse findById(Integer id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên có ID " + id
                ));
    }

    public SaveEmployeeRequest getSaveRequest(Integer id) {
        EmployeeResponse e = findById(id);
        return SaveEmployeeRequest.builder()
                .id(e.getId())
                .name(e.getName())
                .role(e.getRole())
                .age(e.getAge())
                .salary(e.getSalary())
                .departmentId(e.getDepartmentId())
                .positionId(e.getPositionId())
                .build();
    }

    public EmployeeResponse save(Integer id, SaveEmployeeRequest request) {
        if (id != null)
            findById(id);
        if (!departmentRepository.existsById(request.getDepartmentId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phòng ban không tồn tại");
        if (!positionRepository.existsById(request.getPositionId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức vụ không tồn tại");

        Integer employeeId = (id != null) ? id : employeeRepository.nextId();
        Employee employee = Employee.builder()
                .employeeId(employeeId)
                .name(request.getName().trim())
                .role(request.getRole())
                .age(request.getAge())
                .salary(request.getSalary())
                .positionId(request.getPositionId())
                .build();

        if (id == null)
            employeeRepository.insert(request.getDepartmentId(), employee);
        else
            employeeRepository.update(request.getDepartmentId(), employee);
        return findById(employeeId);
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> search(SearchEmployeeRequest request) {
        return employeeMapper.toResponses(employeeRepository.search(
                request.getId(), request.getName(), request.getMinAge(), request.getMaxAge(),
                request.getMinSalary(), request.getPositionId()
        ));
    }

    public List<EmployeeResponse> findByPositionId(Integer positionId) {
        return employeeMapper.toResponses(employeeRepository.findByPositionId(positionId));
    }

    public List<EmployeeResponse> findByDepartmentAndPosition(Integer departmentId, Integer positionId) {
        return employeeMapper.toResponses(
                employeeRepository.findByDepartmentIdAndPositionId(departmentId, positionId)
        );
    }

    public List<EmployeeResponse> findTopSalary(Integer departmentId, int n) {
        return employeeMapper.toResponses(
                employeeRepository.findTopByDepartmentIdOrderBySalaryDesc(departmentId, n)
        );
    }
}
