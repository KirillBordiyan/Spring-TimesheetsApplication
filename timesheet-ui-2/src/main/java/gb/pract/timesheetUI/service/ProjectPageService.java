package gb.pract.timesheetUI.service;

import gb.pract.timesheetUI.pageDTO.ProjectPageDTO;
import gb.pract.timesheetUI.restClient.EmployeeResponse;
import gb.pract.timesheetUI.restClient.ProjectResponse;
import gb.pract.timesheetUI.restClient.TimesheetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProjectPageService {

    private final DiscoveryClient discoveryClient;
    private final TimesheetPageService timesheetPageService;
    private final EmployeePageService employeePageService;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("TIMESHEET-REST");
        //TODO это вариант балансировки, типа берем рандомный из списка TIMESHEET-REST
        int instancesCount = instances.size();
        int instantIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instantIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();

        return RestClient.create(uri);
    }

    public Optional<ProjectPageDTO> findById(Long id) {
        try {
            ProjectResponse projectResponse = restClient().get()
                    .uri("/api/projects/" + id)
                    .retrieve()
                    .body(ProjectResponse.class);

            return Optional.of(convertProject(projectResponse));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public List<ProjectPageDTO> findAll() {
        List<ProjectResponse> responseList = restClient().get()
                .uri("/api/projects")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProjectResponse>>() {
                });

        return responseList.stream()
                .map(this::convertProject)
                .toList();
    }

    public ProjectPageDTO convertProject(ProjectResponse project) {

        ProjectPageDTO projectDTO = new ProjectPageDTO();

        projectDTO.setId(String.valueOf(project.getProjectId()));
        projectDTO.setName(String.valueOf(project.getName()));

        //заполняем список таймшитов
        List<TimesheetResponse> timesheetResponseList = restClient().get()
                .uri("/api/timesheets")
                .retrieve()
                .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
                });

        projectDTO.setTimesheetList(timesheetResponseList.stream()
                .filter(timesheet -> timesheet.getProjectId().equals(project.getProjectId()))
                .map(timesheetPageService::convertTimesheet)
                .toList()
        );

        //заполняем список сотрудников
        List<EmployeeResponse> employeeResponseList = restClient().get()
                .uri("/api/employees")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EmployeeResponse>>() {
                }); // взяли всех сотрудников

        projectDTO.setEmployeeList(employeeResponseList.stream()
                .filter(employee -> projectDTO.getTimesheetList().stream()
                        .anyMatch(ts -> Long.parseLong(ts.getEmployeeId()) == employee.getEmployeeId())
                )//отфильтровали всех по условию, что id сотрудника == id сотрудника в соответствующем таймшите
                .map(employeePageService::convertEmployee)
                .toList());

        return projectDTO;
    }
}
