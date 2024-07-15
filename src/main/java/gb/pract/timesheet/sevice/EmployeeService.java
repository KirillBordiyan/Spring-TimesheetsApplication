package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.EmployeeRepository;
import gb.pract.timesheet.repository.ProjectRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TimesheetRepository timesheetRepository;


    public List<Employee> findAll(Boolean stillWork) {
        if(Objects.equals(stillWork, true)){
           return employeeRepository.findByWorkStatus();
        }
        return employeeRepository.findAll();
    }


    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        if(Objects.isNull(employee.getFullName())){
            throw new IllegalArgumentException("Full name must not be null");
        }
        employee.setStillWork(true);
        employee.setProjectList(new ArrayList<>());
        employee.setTimesheetList(new ArrayList<>());
        employeeRepository.save(employee);

        //TODO здесь сделать так, чтобы при добавлении работника ему приписывалось что-то
        // по аналогии с сервисом таймшитов, где при создании шита он крепится к проекту
//        Optional<Timesheet> timesheetOPT = timesheetRepository.findById(employee.getEmployeeId());
//
//        timesheetOPT.ifPresent(timesheet -> {
//            Long timesheetId = timesheet.getId();
//            if(timesheetRepository.addTimesheet(timesheetId, employee)){
//                timesheetRepository.save(timesheet);
//            }
//
//            if(projectRepository.addTimesheet(projectId, timesheet)){
//                projectRepository.save(project);
//            }
//        });

        return employee;
    }

    public void deleteById(Long id) {
        employeeRepository.findAll().stream()
                .filter(employee -> Objects.equals(employee.getEmployeeId(), id))
                .forEach(employee -> employee.setStillWork(false));

        employeeRepository.deleteById(id);
    }
}
