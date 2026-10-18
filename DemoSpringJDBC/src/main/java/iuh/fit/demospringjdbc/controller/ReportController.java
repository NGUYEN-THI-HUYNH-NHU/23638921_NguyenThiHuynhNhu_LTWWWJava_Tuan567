/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringjdbc.controller;

import iuh.fit.demospringjdbc.entity.Department;
import iuh.fit.demospringjdbc.entity.Position;
import iuh.fit.demospringjdbc.service.DepartmentService;
import iuh.fit.demospringjdbc.service.EmployeeService;
import iuh.fit.demospringjdbc.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    @ModelAttribute("departments")
    public List<Department> departments() {
        return departmentService.findAll();
    }

    @ModelAttribute("positions")
    public List<Position> positions() {
        return positionService.findAll();
    }

    @GetMapping
    public String index(Model model) {
        addOverview(model);
        return "reports/index";
    }

    @GetMapping("/by-position")
    public String byPosition(@RequestParam Integer positionId, Model model) {
        addOverview(model);
        model.addAttribute("resultTitle", "Nhân viên theo chức vụ #" + positionId);
        model.addAttribute("result", employeeService.findByPositionId(positionId));
        return "reports/index";
    }

    @GetMapping("/by-department-position")
    public String byDepartmentPosition(@RequestParam Integer departmentId,
                                       @RequestParam Integer positionId, Model model) {
        addOverview(model);
        model.addAttribute("resultTitle", "Nhân viên thuộc phòng #" + departmentId + " và chức vụ #" + positionId);
        model.addAttribute("result", employeeService.findByDepartmentAndPosition(departmentId, positionId));
        return "reports/index";
    }

    @GetMapping("/top-salary")
    public String topSalary(@RequestParam Integer departmentId,
                            @RequestParam(defaultValue = "5") int n, Model model) {
        addOverview(model);
        model.addAttribute("resultTitle", "Top " + n + " lương cao nhất của phòng #" + departmentId);
        model.addAttribute("result", employeeService.findTopSalary(departmentId, n));
        return "reports/index";
    }

    private void addOverview(Model model) {
        model.addAttribute("countStats", departmentService.countEmployeesByDepartment());
        model.addAttribute("salaryStats", departmentService.salaryStatsByDepartment());
        model.addAttribute("emptyDepartments", departmentService.findWithoutEmployees());
    }
}
