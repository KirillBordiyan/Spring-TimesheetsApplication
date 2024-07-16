package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> findAllEmployees(@RequestParam(required = false) Boolean stillWork) {
        if (Objects.equals(stillWork, null)) {
            stillWork = true;
            //TODO если не указываем, что нужны все - ищем только работающих
            // если придет false, то его и передадим
        }
        return ResponseEntity.ok(employeeService.findAll(stillWork));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(value -> ResponseEntity
                        .status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> findTimesheetsByEmployeeId(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(el -> ResponseEntity.status(HttpStatus.OK).body(el.getTimesheetList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        employee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
