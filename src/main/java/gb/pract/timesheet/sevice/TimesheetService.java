package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.EmployeeRepository;
import gb.pract.timesheet.repository.ProjectRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;


    public Optional<Timesheet> findById(java.lang.Long id) {
        return timesheetRepository.findById(id);
    }

    //TODO переделать с границами
    public List<Timesheet> findAll() {
        return timesheetRepository.findAll();
    }

    public Timesheet saveTimesheet(Timesheet timesheet) {

        if (Objects.isNull(timesheet.getProjectId()) || Objects.isNull(timesheet.getEmployeeId())) {
            throw new IllegalArgumentException("Project ID & Employee ID must not be null");
        }
        if (projectRepository.findById(timesheet.getProjectId()).isEmpty()) {
            throw new NoSuchElementException("Project with ID " + timesheet.getProjectId() + " does not exists");
        }
        if (employeeRepository.findById(timesheet.getEmployeeId()).isEmpty()) {
            throw new NoSuchElementException("Employee with ID " + timesheet.getEmployeeId() + " does not exists");
        }

        timesheet.setCreatedAt(LocalDate.now());
        timesheetRepository.save(timesheet);

        Project projectInnerDB = projectRepository.getReferenceById(timesheet.getProjectId());
        Employee employeeInnerDB = employeeRepository.getReferenceById(timesheet.getEmployeeId());

        Long projectId = projectInnerDB.getProjectId();
        Long employeeId = employeeInnerDB.getEmployeeId();

        try{
            projectRepository.addProjectParam(projectId, timesheet, Project::getTimesheetList);
            projectRepository.addProjectParam(projectId, employeeInnerDB, Project::getEmployeeList);
            employeeRepository.addEmployeeParam(employeeId, timesheet, Employee::getTimesheetList);
            employeeRepository.addEmployeeParam(employeeId, projectInnerDB, Employee::getProjectList);
        } finally {
            projectRepository.save(projectInnerDB);
            employeeRepository.save(employeeInnerDB);
        }



//        projectInnerDB.ifPresent(project -> {
//            Long projectId = project.getProjectId();
//            if (projectRepository.addProjectParam(projectId, timesheet, Project::getTimesheetList)) {
//                projectRepository.save(project);
//            }
//        });

//        employeeInnerDB.ifPresent(employee -> {
//            Long employeeId = employee.getEmployeeId();
//            if (employeeRepository.addEmployeeParam(employeeId, timesheet, Employee::getTimesheetList)) {
//                employeeRepository.save(employee);
//            }
//        });


        return timesheet;
    }

    public void delete(java.lang.Long id) {
        timesheetRepository.deleteById(id);
    }
}
