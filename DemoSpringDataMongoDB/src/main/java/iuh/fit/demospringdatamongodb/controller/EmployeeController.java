/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/18/2026
 */

package iuh.fit.demospringdatamongodb.controller;

import iuh.fit.demospringdatamongodb.dto.request.SaveEmployeeRequest;
import iuh.fit.demospringdatamongodb.dto.request.SearchEmployeeRequest;
import iuh.fit.demospringdatamongodb.dto.response.EmployeeResponse;
import iuh.fit.demospringdatamongodb.entity.Department;
import iuh.fit.demospringdatamongodb.entity.Position;
import iuh.fit.demospringdatamongodb.service.DepartmentService;
import iuh.fit.demospringdatamongodb.service.EmployeeService;
import iuh.fit.demospringdatamongodb.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
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
    public String list(@ModelAttribute("criteria") SearchEmployeeRequest request, Model model) {
        boolean searching = request.hasAny();
        model.addAttribute("employees",
                searching ? employeeService.search(request) : employeeService.findAll());
        model.addAttribute("searching", searching);
        return "employees/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("employeeForm", new SaveEmployeeRequest());
        return "employees/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("employeeForm") SaveEmployeeRequest request,
                         BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors())
            return "employees/form";
        EmployeeResponse saved = employeeService.save(null, request);
        redirect.addFlashAttribute("message", "Đã thêm nhân viên " + saved.getName());
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("employeeForm", employeeService.getSaveRequest(id));
        return "employees/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("employeeForm") SaveEmployeeRequest request,
                         BindingResult result, RedirectAttributes redirect) {
        request.setId(id);
        if (result.hasErrors())
            return "employees/form";
        EmployeeResponse saved = employeeService.save(id, request);
        redirect.addFlashAttribute("message", "Đã lưu thay đổi của nhân viên " + saved.getName());
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirect) {
        employeeService.delete(id);
        redirect.addFlashAttribute("message", "Đã xoá nhân viên có ID " + id);
        return "redirect:/employees";
    }
}
