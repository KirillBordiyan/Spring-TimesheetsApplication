package gb.pract.timesheetUI.restClient;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class ProjectResponse {

    private Long projectId;
    private String name;
    private List<TimesheetResponse> timesheetList;
    private List<EmployeeResponse> employeeList;
}
