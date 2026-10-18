/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringjdbc.service;

import iuh.fit.demospringjdbc.dto.request.SaveEmployeeRequest;
import iuh.fit.demospringjdbc.dto.request.SearchEmployeeRequest;
import iuh.fit.demospringjdbc.dto.response.EmployeeResponse;
import iuh.fit.demospringjdbc.entity.Employee;
import iuh.fit.demospringjdbc.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll();
    }

    public EmployeeResponse findById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() ->
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
    public EmployeeResponse save(Integer id, SaveEmployeeRequest request) {
        Employee employee = Employee.builder()
                .id(id)
                .name(request.getName())
                .role(request.getRole())
                .age(request.getAge())
                .salary(request.getSalary())
                .departmentId(request.getDepartmentId())
                .positionId(request.getPositionId())
                .build();
        return employeeRepository.save(employee);
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> search(SearchEmployeeRequest request) {
        return employeeRepository.search(request);
    }

    public List<EmployeeResponse> findByPositionId(Integer positionId) {
        return employeeRepository.findByPositionId(positionId);
    }

    public List<EmployeeResponse> findByDepartmentAndPosition(Integer departmentId, Integer positionId) {
        return employeeRepository.findByDepartmentIdAndPositionId(departmentId, positionId);
    }

    public List<EmployeeResponse> findTopSalary(Integer departmentId, int n) {
        return employeeRepository.findTopByDepartmentIdOrderBySalaryDesc(departmentId, n);
    }
}
