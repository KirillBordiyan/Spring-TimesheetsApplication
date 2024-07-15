package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InputDataInitializer implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
//
//        for (int i = 1; i <= 100; i++){
//            Employee employee = new Employee();
//            employee.setProjectList(new ArrayList<>());
//            employee.setTimesheetList(new ArrayList<>());
//            employee.setFullName("EmployeeName "+ i);
//            employee.setStillWork(ThreadLocalRandom.current().nextBoolean());
//            employeeRepository.save(employee);
//        }



//        for(int i = 1; i <= 5; i++){
//            Project project = new Project();
//            project.setName("Project #" + i);
//            project.setTimesheetList(new ArrayList<>());
//            project.setEmployeeList(new ArrayList<>());
//            projectRepository.save(project);
//        }
//
//        for(int i = 1; i <= 30; i++){
//            Timesheet timesheet = new Timesheet();
//            timesheet.setProjectId(random.nextLong(1L, 6L));
//            timesheet.setCreatedAt(LocalDate.now());
//            timesheet.setMinutes(ThreadLocalRandom.current().nextInt(200, 1000));
//            timesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 101));
//            timesheetRepository.save(timesheet);
//        }


    }
}
