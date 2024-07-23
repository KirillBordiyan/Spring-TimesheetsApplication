package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Для взаимодействия с Employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Get all employees",
            description = """
                    Метод получения списка сотрудников.
                    По умолчанию будет происходить поиск только тех,кто числится в штате.
                    Чтобы вывести всех нужно указать параметр stillWork=false""",

            tags = {"Get", "Employee"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Employee.class))),
                    //TODO оставил, тк если вдруг искать вручную будет 400 из-за неправильного параметра
                    @ApiResponse(
                            responseCode = "500",
                            description = "Некорректные параметры запроса (stillWork !=true/false)",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<Employee>> findAllEmployees(@Parameter(description = "Учитывать работающих на данный момент или нет")
                                                               @RequestParam(required = false) Boolean stillWork) {
        if (Objects.equals(stillWork, null)) {
            stillWork = true;
        }
        return ResponseEntity.ok(employeeService.findAll(stillWork));
    }

    @Operation(
            summary = "Get employee by id",
            description = "Метод получения сотрудника по id",
            tags = {"Get", "Employee"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Employee.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Сотрудник с таким ID не найден",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(value -> ResponseEntity
                        .status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get timesheets from employee by employeeID",
            description = "Метод получения списка записей рабочего времени у конкретного сотрудника",
            tags = {"Get", "Employee"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Employee.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Сотрудник с таким ID не найден",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> findTimesheetsByEmployeeId(@Parameter(description = "ID сотрудника")
                                                                          @PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        return employee.map(el -> ResponseEntity.status(HttpStatus.OK).body(el.getTimesheetList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(
            summary = "Post employee",
            description = "Метод для создания нового сотрудника",
            tags = {"Post", "Employee"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Создана запись",
                            content = @Content(schema = @Schema(implementation = Employee.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса (тело сотрудника)",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Parameter(description = "Тело создаваемого объекта")
                                                     @RequestBody Employee employee) {
        employee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @Operation(
            summary = "Delete project",
            description = """
                    Метод для удаления проекта.
                    Аналог методу удаления.
                    Иза-за особенности логики мы меняем состояние объекта, не удаляя его из БД""",
            tags = {"Delete", "Employee"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Запись преобразована",
                            content = @Content(schema = @Schema(implementation = Employee.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateEmployee(@Parameter(description = "ID для удаления сотрудника (увольнения)")
                                                       @PathVariable Long id) {
        employeeService.deactivateEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
