package gb.pract.timesheet.page;

import lombok.Data;

import java.util.List;

@Data
public class ProjectPageDTO {
    private String id;
    private String name;
    private List<TimesheetPageDTO> timesheets;
}
