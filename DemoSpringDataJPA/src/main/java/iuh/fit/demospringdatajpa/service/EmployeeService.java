/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatajpa.service;

import iuh.fit.demospringdatajpa.dto.request.SaveEmployeeRequest;
import iuh.fit.demospringdatajpa.dto.request.SearchEmployeeRequest;
import iuh.fit.demospringdatajpa.dto.response.EmployeeResponse;
import iuh.fit.demospringdatajpa.entity.Department;
import iuh.fit.demospringdatajpa.entity.Employee;
import iuh.fit.demospringdatajpa.entity.Position;
import iuh.fit.demospringdatajpa.mapper.EmployeeMapper;
import iuh.fit.demospringdatajpa.repository.DepartmentRepository;
import iuh.fit.demospringdatajpa.repository.EmployeeRepository;
import iuh.fit.demospringdatajpa.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên có ID " + id));
    }

    /**
     * Nạp dữ liệu hiện có vào request để đổ lên form sửa.
     */
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

    /**
     * id == null: thêm mới; id != null: cập nhật.
     */
    @Transactional
    public EmployeeResponse save(Integer id, SaveEmployeeRequest request) {
        if (id != null && !employeeRepository.existsById(id))
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên có ID " + id);
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Phòng ban không tồn tại"));
        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Chức vụ không tồn tại"));

        Employee employee = Employee.builder()
                .id(id)
                .name(request.getName().trim())
                .role(request.getRole())
                .age(request.getAge())
                .salary(request.getSalary())
                .department(department)
                .position(position)
                .build();
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> search(SearchEmployeeRequest request) {
        String name = (request.getName() == null || request.getName().isBlank()
                ? null : request.getName());
        return employeeMapper.toResponses(employeeRepository.search(
                request.getId(), name, request.getMinAge(), request.getMaxAge(),
                request.getMinSalary(), request.getPositionId()
        ));
    }

    public List<EmployeeResponse> findByPositionId(Integer positionId) {
        return employeeMapper.toResponses(employeeRepository.findByPositionId(positionId));
    }

    public List<EmployeeResponse> findByDepartmentAndPosition(Integer departmentId, Integer positionId) {
        return employeeMapper.toResponses(employeeRepository.findByDepartmentIdAndPositionId(departmentId, positionId));
    }

    public List<EmployeeResponse> findTopSalary(Integer departmentId, int n) {
        return employeeMapper.toResponses(employeeRepository
                .findByDepartmentIdOrderBySalaryDesc(departmentId, PageRequest.of(0, n)));
    }
}
