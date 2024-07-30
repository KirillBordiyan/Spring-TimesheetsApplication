package gb.pract.timesheetRest.service.page;

import gb.pract.timesheetRest.model.Employee;
import gb.pract.timesheetRest.model.Project;
import gb.pract.timesheetRest.model.Timesheet;
import gb.pract.timesheetRest.pageDTO.TimesheetPageDTO;
import gb.pract.timesheetRest.service.EmployeeService;
import gb.pract.timesheetRest.service.ProjectService;
import gb.pract.timesheetRest.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {

    private final TimesheetService timesheetService;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public Optional<TimesheetPageDTO> findById(Long id) {
        return timesheetService.findById(id)
                .map(this::convertTimesheet);
    }

    public List<TimesheetPageDTO> findAll(LocalDate createdAfter, LocalDate createdBefore) {
        return timesheetService.findAll(createdAfter, createdBefore).stream()
                .map(this::convertTimesheet)
                .toList();
    }

    public TimesheetPageDTO convertTimesheet(Timesheet timesheet) {
        Optional<Project> projectInnerDB = projectService.findById(timesheet.getProjectId());
        Optional<Employee> employeeInnerDB = employeeService.findById(timesheet.getEmployeeId());

        TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();

        timesheetPageDTO.setId(String.valueOf(timesheet.getId()));
        timesheetPageDTO.setCreatedAt(String.valueOf(timesheet.getCreatedAt()));
        timesheetPageDTO.setMinutes(String.valueOf(timesheet.getMinutes()));

        projectInnerDB.ifPresent(project -> {
            timesheetPageDTO.setProjectId(String.valueOf(project.getProjectId()));
            timesheetPageDTO.setProjectName(project.getName());
        });

        employeeInnerDB.ifPresent(employee -> {
            timesheetPageDTO.setEmployeeId(String.valueOf(employee.getEmployeeId()));
            timesheetPageDTO.setEmployeeName(employee.getFullName());
        });


        return timesheetPageDTO;
    }
}
