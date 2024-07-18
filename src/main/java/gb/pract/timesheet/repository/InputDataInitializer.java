package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.EmployeeService;
import gb.pract.timesheet.sevice.ProjectService;
import gb.pract.timesheet.sevice.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InputDataInitializer implements CommandLineRunner {

    private final ProjectService projectService;
    private final TimesheetService timesheetService;
    private final EmployeeService employeeService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        for (int i = 1; i <= 100; i++) {
            Employee employee = new Employee();
            employee.setFullName("Employee #" + i);
            employeeService.saveEmployee(employee);
        }

        for (int i = 1; i <= 10; i++) {
            Project project = new Project();
            project.setName("Project #" + i);
            projectService.saveProject(project);
        }

        for (int i = 1; i <= 50; i++) {
            Timesheet timesheet = new Timesheet();
            timesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 11));
            timesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 101));
            timesheet.setMinutes(ThreadLocalRandom.current().nextInt(200, 1000));

            timesheetService.saveTimesheet(timesheet);
        }
    }
}
