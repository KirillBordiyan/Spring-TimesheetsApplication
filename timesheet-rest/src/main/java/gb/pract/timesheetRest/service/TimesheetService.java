package gb.pract.timesheetRest.service;

import gb.pract.aspect.logging.Logging;
import gb.pract.aspect.recover.Recover;
import gb.pract.timesheetRest.aop.myAnno.Timer;
import gb.pract.timesheetRest.model.Employee;
import gb.pract.timesheetRest.model.Project;
import gb.pract.timesheetRest.model.Timesheet;
import gb.pract.timesheetRest.repository.EmployeeRepository;
import gb.pract.timesheetRest.repository.ProjectRepository;
import gb.pract.timesheetRest.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Timer(enabled = false)
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Logging
    @Recover(noRecovered = {ClassNotFoundException.class})
    public Optional<Timesheet> findById(Long id) {
        throw new ArithmeticException("выброшена в нужном месте");
//        return timesheetRepository.findById(id);
    }

    @Logging(printArgsInside = false)
    @Recover
    public List<Timesheet> findAll(LocalDate createdAfter, LocalDate createdBefore) {
        if (createdBefore == null && createdAfter == null) {
            return timesheetRepository.findAll();
        }
        if (Objects.isNull(createdAfter)) {
            return timesheetRepository.findByCreatedAtBefore(createdBefore);
        }
        if (Objects.isNull(createdBefore)) {
            return timesheetRepository.findByCreatedAtAfter(createdAfter);
        }
        return timesheetRepository.findByCreatedAtBetween(createdAfter, createdBefore);
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

        try {
            projectInnerDB.getTimesheetList().add(timesheet);
            projectInnerDB.getEmployeeList().add(employeeInnerDB);

            employeeInnerDB.getTimesheetList().add(timesheet);
            employeeInnerDB.getProjectList().add(projectInnerDB);

        } finally {
            projectRepository.save(projectInnerDB);
            employeeRepository.save(employeeInnerDB);
        }
        return timesheet;
    }

    public void delete(Long id) {
        Optional<Timesheet> timesheet = timesheetRepository.findById(id);

        timesheet.ifPresent(timesheetI -> {
            projectRepository.findById(
                    timesheetI.getProjectId()).ifPresent(
                    project -> {
                        project.getTimesheetList().remove(timesheetI);
                        projectRepository.save(project);
                    }
            );
            employeeRepository.findById(
                    timesheetI.getEmployeeId()).ifPresent(
                    employee -> {
                        employee.getTimesheetList().remove(timesheetI);
                        employeeRepository.save(employee);
                    });

        });

        timesheetRepository.deleteById(id);
    }
}
