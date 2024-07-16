package gb.pract.timesheet.page.pageDTO;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
public class ProjectPageDTO {
    private String id;
    private String name;
    private List<TimesheetPageDTO> timesheetList;
//    @JsonIgnoreProperties(value = {"timesheetList"})
//FIXME здесь МОЖНО блокировать поля, например, projectList и timesheetList,
// в объектах EmployeePageDTO на ПРИ КРУГОВОЙ СВЯЗИ - аннотация выше
    private List<EmployeePageDTO> employeeList;
}
