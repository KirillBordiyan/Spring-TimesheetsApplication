package gb.pract.timesheet.service;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(classes = {ProjectRepository.class, ProjectService.class})
@ActiveProfiles("test")
@SpringBootTest
class ProjectServiceTest {

    /**
     * Это тест на сервис
     * <br><br>
     * Попробуем написать тест для метода findById у сервиса проектов;
     * <br>
     * нужно понимать, что у сервиса есть свои репозитории и др сервисы, с которыми он взаимодействует;
     * Но мы также не должны зависеть от их реализации (знчт надо подменить);<br>
     * Тк это спринговое приложение - нужен контекст, его поднять поможет @SpringBootTest.<br>
     * Там можно указать список классов-бинов, которые нужны.
     * Либо не писать ничего - спринг сам все сделает
     * (найдет точку входа, с которой создается класс, и последовательно все поднимет)<br><br>
     * <p>
     * Если НЕ указываем ничего - создает все бины в контексте<br>
     * Если указываем конкретные - поднимет только их<br><br>
     * <p>
     * После всего их можно @Autowired в наш тест, НО
     * если у нас поднмиается бин репозитория, который, как у нас JPA-реп, то так не сработает<br><br>
     * нужно прописать не @Autowired, а @MockBean, чтобы это был "подставной" бин<br><br>
     * (есть @InjectMocks, нужна, чтобы добавить моки, которые созданы @Mock)<br><br>
     * Если нам нужна связь с БД, можно написать .yaml в тестах
     */

//    @MockBean
    @Autowired
    ProjectService projectService;

//    @MockBean
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void testFindByIdEmpty(){

        //given
        //проверим, что БД пустая
        assertFalse(projectRepository.existsById(2L));

        //когда бд пустая
        //но нет гарантии, что в БД ничего до него не запишется (добавим то, что выше)
        assertTrue(projectService.findById(2L).isEmpty());
    }

    @Test
    void testFindProjectById() {
        //given
        Project project = new Project();
        project.setName("projectName");
        project.setTimesheetList(new ArrayList<>());
        project.setEmployeeList(new ArrayList<>());

        project = projectRepository.save(project);

        //when
        Optional<Project> actual = projectService.findById(project.getProjectId());


        //then
        //проверяем, а представлен ли актуал по поиску
        assertTrue(actual.isPresent());
        //сравниваем, что id по поиску равен сохраненному id
        assertEquals(project.getProjectId(), actual.get().getProjectId());
        //также проверяем сходство имени
        assertEquals(project.getName(),actual.get().getName() );

        //верни пустой Optional, когда вызовут метод поиска по id = 2L
//        doReturn(Optional.empty()).when(projectRepository).findById(2L);
//        assertTrue(projectService.findById(2L).isEmpty());
        //репозиторий вернет пустой Optional, который заберет сервис и мы проверяем, что тот Optional, который он забрал - реально пустой
    }
}