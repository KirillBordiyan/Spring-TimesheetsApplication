package gb.pract.timesheet.controller;

import gb.pract.timesheet.controller.jwt.LoginForm;
import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static reactor.core.publisher.Mono.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = /*добавим случайный порт*/ SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient //нужен для webTestClient
class ProjectControllerTest {

    @Autowired
    ProjectRepository projectRepository;


//    @Autowired
//    WebTestClient webTestClient; //тогда порт НЕ надо указывать отдельно


    @LocalServerPort //инжектим тот порт, который спринг нам дал из webEnv
    private int port;
    private RestClient restClient;

    @BeforeEach
    void beforeEach() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void testDeleteProject(){
        Project preparedToDelete = new Project();
        preparedToDelete.setName("prepared");
        preparedToDelete.setEmployeeList(new ArrayList<>());
        preparedToDelete.setTimesheetList(new ArrayList<>());

        preparedToDelete = projectRepository.save(preparedToDelete);

        ResponseEntity<Void> response = restClient.delete()
                .uri("/api/projects/" + preparedToDelete.getProjectId())
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .toBodilessEntity();

        //проверка статуса NO_CONTENT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        //проверка, что записи УЖЕ НЕТ
        assertFalse(projectRepository.existsById(preparedToDelete.getProjectId()));
    }


    @Test
    void testCreateProject() {
        Project toSave = new Project();
        toSave.setName("saveProject");

        ResponseEntity<Project> postResponse = restClient.post()
                .uri("/api/projects")
                .header("Authorization", "Bearer " + getJWT())
                .body(toSave)
                .retrieve()
                .toEntity(Project.class);

        //проверка соответствия кода
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        //проверка на наличие записи в БД
        assertTrue(projectRepository.existsById(postResponse.getBody().getProjectId()));

    }

    @Test
    void testGetProjectNotFound() {

        assertThrows(HttpClientErrorException.NotFound.class, () -> {

            ResponseEntity<Void> response = restClient.get()
                    .uri("/api/projects/-2")
                    .header("Authorization", "Bearer " + getJWT())
                    .retrieve()
                    .toBodilessEntity();
        });
    }


    @Test
    void testGetProjectById() {
        Project project = new Project();
        project.setName("projectName");
        project.setTimesheetList(new ArrayList<>());
        project.setEmployeeList(new ArrayList<>());
        Project expected = projectRepository.save(project);
//     TODO
//      Mono / Flux - определения из концепции реактивного программирования
//      Моно получает либо 1, либо 0 аргументов
//      Flux получает что-то порционно, списки
//      Это подход, чтобы не ждать выполнения всего, а брать сразу то, что есть
//      Они заполнятся как бы "попозже", либо не заполнятся

//        /*Mono<*/ResponseEntity<Project>/*>*/ entity = WebClient.create().get()
//                .uri()
//                .retrieve()
//                .toEntity(Project.class)
//                .block();//если хоти вернуть сразу ResponseEntity<?>
        //или
//        entity.subscribe(it -> {
//            ...
//        }) // если вернули Mono<> и надо оттуда достать содержимое

//        TODO после добавления аннотации @Auto...
//         у нас появляется бин WebTestClient
//         ну понятно, что в нашем случае надо сделать доп запрос для получения jwt:
//        webTestClient.get()
//                .uri()
//                .exchange() // retrieve
//                .expectStatus().isOk()
//                .expectBody(Project.class)
//                .value(actual -> { // actual это УЖЕ наш проект
//                    assertNotNull(actual);
//                    //id совпадают
//                    assertEquals(expected.getProjectId(), actual.getProjectId());
//                    //имя совпадает
//                    assertEquals(expected.getName(), actual.getName());
//                });
//        TODO
//         как бы разница в них это наличие assert() внутри webTestClient и тот факт, что он же это как бы условное соединение
//         при RestClient делается полный запрос, а проверки соответствия происходят на основе полученных данных

//        TODO есть аналогичная штука - MockMvc со своей реализацией
//         важно понимать, что они в целом про одно и то же


        ResponseEntity<Project> actual = restClient.get()
                .uri("/api/projects/" + expected.getProjectId()) //учитываем, что это не микросервисы
                .header("Authorization", "Bearer " + getJWT())
                .retrieve()
                .toEntity(Project.class);


        //assert 200 OK
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        //достаем проект
        Project responseBody = actual.getBody();
        //он не пустой
        assertNotNull(responseBody);
        //id совпадают
        assertEquals(project.getProjectId(), responseBody.getProjectId());
        //имя совпадает
        assertEquals(project.getName(), responseBody.getName());
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