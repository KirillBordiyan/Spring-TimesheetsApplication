package gb.pract.timesheet.controller;

import gb.pract.timesheet.controller.jwt.LoginForm;
import gb.pract.timesheet.model.Employee;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.EmployeeRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetControllerTest {

    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    private RestClient restClient;
    @LocalServerPort
    private int port;


    @BeforeEach
    void beforeEach() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void testGetTimesheetById() {

        Timesheet timesheetCreate = new Timesheet();
        timesheetCreate.setMinutes(100);
        timesheetCreate.setProjectId(1L);
        timesheetCreate.setEmployeeId(1L);
        Timesheet expected = timesheetRepository.save(timesheetCreate);


        ResponseEntity<Timesheet> actualResponse = restClient.get()
                .uri("/api/timesheets/" + expected.getId())
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .toEntity(Timesheet.class);

        //код 200 + тело не пустое + id совпадают + id проектов совпадают + в БД запись есть
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertNotNull(actualResponse.getBody());
        assertEquals(expected.getId(), actualResponse.getBody().getId());
        assertEquals(expected.getProjectId(), actualResponse.getBody().getProjectId());
        assertTrue(timesheetRepository.existsById(actualResponse.getBody().getId()));
    }

    @Test
    void testFindAllTimesheets() {

        Timesheet timesheetCreate = new Timesheet();
        timesheetCreate.setMinutes(100);
        timesheetCreate.setProjectId(1L);
        timesheetCreate.setEmployeeId(1L);
        timesheetCreate.setCreatedAt(LocalDate.now());
        timesheetRepository.save(timesheetCreate);

        ResponseEntity<List<Timesheet>> timesheetsListResponse = restClient.get()
                .uri("/api/timesheets")
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        //код 200 + результат не List = null
        assertEquals(HttpStatus.OK, timesheetsListResponse.getStatusCode());
        assertNotNull(timesheetsListResponse.getBody());
    }


    @Test
    void testCreateTimesheet() {

        //FIXME этот хотел с моками попробовать, но видимо плохо понял (хотя что там понимать)
        // и не вышло, пробовал несколько раз
        // поправьте, если что-то упустил:
        //      -объекты подогнать по нужным вещам (будто отправляем их JSON в постмане)
        //      -в аннотации SpringBootTest добавляем классы-бины, которые попадут в контекст
        //      -для этих классов (репозиториев, сервисов) пишем @MockBean
        //      -говорим doReturn(employee).when(mockRepository/service).method(args)
        //      -потом сравниваем, что вышло
        // вроде по логике все так, не пойму где ошибся

        Employee createdEmployee = new Employee();
        createdEmployee.setFullName("employee test");
        createdEmployee.setStillWork(true);
        createdEmployee.setTimesheetList(new ArrayList<>());
        createdEmployee.setProjectList(new ArrayList<>());
        Employee employeeExpected = employeeRepository.save(createdEmployee);

        Timesheet createdTimesheet = new Timesheet();
        createdTimesheet.setCreatedAt(LocalDate.now());
        createdTimesheet.setMinutes(120);
        createdTimesheet.setProjectId(1L);
        createdTimesheet.setEmployeeId(employeeExpected.getEmployeeId());

        ResponseEntity<Timesheet> postResponse = restClient.post()
                .uri("/api/timesheets")
                .header("Authorization", "Bearer " + getJWT())
                .body(createdTimesheet)
                .retrieve()
                .toEntity(Timesheet.class);

        Employee actualEmployeeResponse = restClient.get()
                .uri("/api/employees/" + employeeExpected.getEmployeeId())
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .body(Employee.class);


        assertNotNull(actualEmployeeResponse);
        assertNotNull(postResponse.getBody());
        List<Timesheet> employeesTimesheetList = actualEmployeeResponse.getTimesheetList();
        Timesheet timesheetResponseBody = postResponse.getBody();

        //статус создания + запись существует
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertTrue(timesheetRepository.existsById(timesheetResponseBody.getId()));
        //созданный таймшит имеет тот же id, что новый сотрудник
        assertEquals(timesheetResponseBody.getEmployeeId(), employeeExpected.getEmployeeId());
        //id сотрудника == id сотрудника в таймшите (последнего добавленного)
        assertEquals(employeesTimesheetList.get(employeesTimesheetList.size() - 1).getEmployeeId(),
                actualEmployeeResponse.getEmployeeId());
    }

    @Test
    void testDeleteTimesheetById() {

        Timesheet delete = new Timesheet();
        delete.setCreatedAt(LocalDate.now());
        delete.setMinutes(120);
        delete.setProjectId(1L);
        delete.setEmployeeId(1L);
        Timesheet expected = timesheetRepository.save(delete);


        ResponseEntity<Void> deleteResponse = restClient.delete()
                .uri("/api/timesheets/" + expected.getId())
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .toBodilessEntity();

        //код 204 + записи уже нет
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertFalse(timesheetRepository.existsById(expected.getId()));
    }

    private String getJWT() {
        LoginForm loginForm = new LoginForm("user3", "user3");
        String jwt = restClient.post()
                .uri("/authenticate")
                .body(loginForm)
                .retrieve()
                .toEntity(String.class).getBody();
        System.out.println(jwt);
        return jwt;
    }
}