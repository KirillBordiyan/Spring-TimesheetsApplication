package gb.pract.timesheetUI.service;

import gb.pract.timesheetUI.pageDTO.TimesheetPageDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {

    private final DiscoveryClient discoveryClient;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("TIMESHEET-REST");
        //TODO это вариант балансировки, типа берем рандомный из списка TIMESHEET-REST
        int instancesCount = instances.size();
        int instantIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instantIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();

        return RestClient.create(uri);
    }

    public Optional<TimesheetPageDTO> findById(Long id) {

        try {
            TimesheetResponse timesheet = restClient().get()
                    .uri("/api/timesheets/" + id)
                    .retrieve()
                    .body(TimesheetResponse.class);

            return Optional.of(convertTimesheet(timesheet));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public List<TimesheetPageDTO> findAll(LocalDate createdAfter, LocalDate createdBefore) {

        List<TimesheetResponse> responseList = restClient().get()
                .uri("/api/timesheets")
                .header("createdAfter", String.valueOf(createdAfter))
                .header("createdBefore", String.valueOf(createdBefore))
                .retrieve()
                .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
                });

        return responseList.stream()
                .map(this::convertTimesheet)
                .toList();
    }

    public TimesheetPageDTO convertTimesheet(TimesheetResponse timesheet) {
        ProjectResponse project = restClient().get()
                .uri("/api/projects/" + timesheet.getProjectId())
                .retrieve()
                .body(ProjectResponse.class);

        EmployeeResponse employee = restClient().get()
                .uri("/api/employees/" + timesheet.getEmployeeId())
                .retrieve()
                .body(EmployeeResponse.class);

        TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();

        timesheetPageDTO.setId(String.valueOf(timesheet.getId()));
        timesheetPageDTO.setCreatedAt(String.valueOf(timesheet.getCreatedAt()));
        timesheetPageDTO.setMinutes(String.valueOf(timesheet.getMinutes()));

        timesheetPageDTO.setProjectId(String.valueOf(project.getProjectId()));
        timesheetPageDTO.setProjectName(project.getName());

        timesheetPageDTO.setEmployeeId(String.valueOf(employee.getEmployeeId()));
        timesheetPageDTO.setEmployeeName(employee.getFullName());

        return timesheetPageDTO;
    }
}
