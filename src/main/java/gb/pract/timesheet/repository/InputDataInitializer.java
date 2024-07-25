package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.*;
import gb.pract.timesheet.service.ClientService;
import gb.pract.timesheet.service.EmployeeService;
import gb.pract.timesheet.service.ProjectService;
import gb.pract.timesheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InputDataInitializer implements CommandLineRunner {

    private final ProjectService projectService;
    private final TimesheetService timesheetService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder coder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {


        Role roleAdmin = new Role();
        Role roleUser = new Role();
        Role roleRest = new Role();
        roleUser.setRoleName("user");
        roleAdmin.setRoleName("admin");
        roleRest.setRoleName("rest");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleRest);

        Client client1 = new Client();
        client1.setLogin("user1");
        client1.setPassword(coder.encode("user1"));
        client1.setRoles(new HashSet<>(List.of(
                roleRepository.getReferenceById(1L)
        )));
        clientService.saveClient(client1);
        System.out.println(client1);

        Client client2 = new Client();
        client2.setLogin("user2");
        client2.setPassword(coder.encode("user2"));
        client2.setRoles(new HashSet<>(List.of(
                roleRepository.getReferenceById(1L),
                roleRepository.getReferenceById(2L)
        )));
        clientService.saveClient(client2);
        System.out.println(client2);


        Client client3 = new Client();
        client3.setLogin("user3");
        client3.setPassword(coder.encode("user3"));
        client3.setRoles(new HashSet<>(List.of(
                roleRepository.getReferenceById(3L)
        )));
        clientService.saveClient(client3);
        System.out.println(client3);


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
