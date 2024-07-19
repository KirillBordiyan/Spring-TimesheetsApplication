package gb.pract.timesheet.page;

import gb.pract.timesheet.page.pageDTO.EmployeePageDTO;
import gb.pract.timesheet.sevice.page.EmployeePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/home/employees")
@RequiredArgsConstructor
public class EmployeePageController {

    private final EmployeePageService employeePageService;

    @GetMapping
    public String getAllEmployees(Model model, @RequestParam(required = false) Boolean stillWork) {
        List<EmployeePageDTO> employees = employeePageService.findAll(stillWork);
        model.addAttribute("employees", employees);
        return "employees-page.html";
    }

    @GetMapping("/{id}")
    public String getEmployeePage(@PathVariable Long id, Model model) {
        Optional<EmployeePageDTO> pageDTO = employeePageService.findById(id);
        if (pageDTO.isEmpty()) {
            throw new NoSuchElementException();
        }

        model.addAttribute("employeeTimesheets", pageDTO.get().getTimesheetList());
        model.addAttribute("employee", pageDTO.get());

        return "employee-page.html";
    }
}
