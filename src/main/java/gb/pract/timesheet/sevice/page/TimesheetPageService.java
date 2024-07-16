package gb.pract.timesheet.sevice.page;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.page.pageDTO.TimesheetPageDTO;
import gb.pract.timesheet.sevice.EmployeeService;
import gb.pract.timesheet.sevice.ProjectService;
import gb.pract.timesheet.sevice.TimesheetService;
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

    public Optional<TimesheetPageDTO> findById(java.lang.Long id) {
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
