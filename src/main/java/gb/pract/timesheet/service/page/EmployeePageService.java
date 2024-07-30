package gb.pract.timesheetRest.service.page;

import gb.pract.timesheetRest.model.Employee;
import gb.pract.timesheetRest.pageDTO.EmployeePageDTO;
import gb.pract.timesheetRest.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeePageService {

    private final EmployeeService employeeService;
    private final TimesheetPageService timesheetPageService;

    public Optional<EmployeePageDTO> findById(Long id) {
        return employeeService.findById(id)
                .map(this::convertEmployee);
    }

    public List<EmployeePageDTO> findAll(Boolean stillWork) {
        return employeeService.findAll(stillWork)
                .stream()
                .map(this::convertEmployee)
                .toList();
    }

    public EmployeePageDTO convertEmployee(Employee employee) {
        EmployeePageDTO employeeDTO = new EmployeePageDTO();

        employeeDTO.setEmployeeId(String.valueOf(employee.getEmployeeId()));
        employeeDTO.setTimesheetList(List.copyOf(employee.getTimesheetList().stream()
                .map(timesheetPageService::convertTimesheet)
                .toList()));
        employeeDTO.setFullName(employee.getFullName());
        employeeDTO.setStillWork(String.valueOf(employee.getStillWork()));

        return employeeDTO;
    }
}
