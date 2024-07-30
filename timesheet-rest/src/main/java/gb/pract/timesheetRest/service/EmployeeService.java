package gb.pract.timesheetRest.service;

import gb.pract.timesheetRest.model.Employee;
import gb.pract.timesheetRest.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll(Boolean stillWork) {
        if (Objects.equals(stillWork, true) || Objects.isNull(stillWork)) {
            return employeeRepository.findByWorkStatus();
        }
        return employeeRepository.findAll();
    }


    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        if (Objects.isNull(employee.getFullName())) {
            throw new IllegalArgumentException("Full name must not be null");
        }
        employee.setStillWork(true);
        employee.setProjectList(new ArrayList<>());
        employee.setTimesheetList(new ArrayList<>());
        employeeRepository.save(employee);

        return employee;
    }

    public void deactivateEmployeeById(Long id) {
        Optional<Employee> employeeInnerDB = employeeRepository.findById(id);

        employeeInnerDB.ifPresent(employee -> {
            employee.setStillWork(false);
            employeeRepository.save(employee);
        });
    }
}
