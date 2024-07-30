package gb.pract.timesheetUI.pageDTO;

import lombok.Data;

import java.util.List;

@Data
public class ProjectPageDTO {
    private String id;
    private String name;
    private List<TimesheetPageDTO> timesheetList;
//    @JsonIgnoreProperties(value = {"timesheetList"})
//TODO здесь МОЖНО блокировать поля, например, projectList и timesheetList,
// в объектах EmployeePageDTO на ПРИ КРУГОВОЙ СВЯЗИ - аннотация выше
    private List<EmployeePageDTO> employeeList;
}
