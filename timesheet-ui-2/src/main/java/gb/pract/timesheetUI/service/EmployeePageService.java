package gb.pract.timesheetUI.service;

import gb.pract.timesheetUI.pageDTO.EmployeePageDTO;
import gb.pract.timesheetUI.restClient.EmployeeResponse;
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
public class EmployeePageService {

    private final DiscoveryClient discoveryClient;
    private final TimesheetPageService timesheetPageService;

    private RestClient restClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("TIMESHEET-REST");
        //TODO это вариант балансировки, типа берем рандомный из списка TIMESHEET-REST
        int instancesCount = instances.size();
        int instantIndex = ThreadLocalRandom.current().nextInt(0, instancesCount);

        ServiceInstance instance = instances.get(instantIndex);
        String uri = "http://" + instance.getHost() + ":" + instance.getPort();

        return RestClient.create(uri);
    }

    public Optional<EmployeePageDTO> findById(Long id) {
        try {
            EmployeeResponse employeeResponse = restClient().get()
                    .uri("/api/employees/" + id)
                    .retrieve()
                    .body(EmployeeResponse.class);

            return Optional.of(convertEmployee(employeeResponse));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    public List<EmployeePageDTO> findAll(Boolean stillWork) {

        List<EmployeeResponse> responseList = restClient().get()
                .uri("/api/employees")
                .header("stillWork", String.valueOf(stillWork))
                .retrieve()
                .body(new ParameterizedTypeReference<List<EmployeeResponse>>() {
                });

        return responseList.stream()
                .map(this::convertEmployee)
                .toList();
    }

    public EmployeePageDTO convertEmployee(EmployeeResponse employee) {
        EmployeePageDTO employeeDTO = new EmployeePageDTO();

        List<TimesheetResponse> timesheetResponseList = restClient().get()
                .uri("/api/timesheets")
                .retrieve()
                .body(new ParameterizedTypeReference<List<TimesheetResponse>>() {
                });

        employeeDTO.setTimesheetList(timesheetResponseList.stream()
                .filter(timesheet -> timesheet.getEmployeeId().equals(employee.getEmployeeId()))
                .map(timesheetPageService::convertTimesheet)
                .toList()
        );

        employeeDTO.setEmployeeId(String.valueOf(employee.getEmployeeId()));
        employeeDTO.setFullName(employee.getFullName());
        employeeDTO.setStillWork(String.valueOf(employee.getStillWork()));


        return employeeDTO;
    }
}
