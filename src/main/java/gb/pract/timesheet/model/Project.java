package gb.pract.timesheet.model;

import lombok.Data;

import java.util.List;

@Data
public class Project {
    private Long project_id;
    private String name;
    private List<Timesheet> timesheetList;
}
