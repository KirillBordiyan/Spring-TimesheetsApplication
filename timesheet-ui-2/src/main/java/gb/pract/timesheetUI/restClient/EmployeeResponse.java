package gb.pract.timesheetUI.restClient;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeResponse {

    private Long employeeId;
    private String fullName;
    private Boolean stillWork;
    private List<TimesheetResponse> timesheetList;
//    private List<ProjectResponse> projectList;
}
