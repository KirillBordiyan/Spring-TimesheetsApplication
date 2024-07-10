package gb.pract.timesheet.page;

import lombok.Data;

@Data
public class TimesheetPageDTO {
    private String id;
    private String minutes;
    private String createdAt;
    private String projectName;
    private String projectId;

}
