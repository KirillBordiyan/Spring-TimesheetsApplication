package gb.pract.timesheet.page.pageDTO;

import lombok.Data;

@Data
public class TimesheetPageDTO {
    private String id;
    private String minutes;
    private String createdAt;
    private String projectName;
    private String projectId;
    private String employeeId;
    private String employeeName;
}
