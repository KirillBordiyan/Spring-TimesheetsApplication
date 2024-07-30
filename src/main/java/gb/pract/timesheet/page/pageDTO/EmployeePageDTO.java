package gb.pract.timesheetRest.pageDTO;

import lombok.Data;

import java.util.List;

@Data
public class EmployeePageDTO {
    private String employeeId;
    private String fullName;
    private String stillWork;
    private List<TimesheetPageDTO> timesheetList;
    //TODO сотрудник не хранит проекты, в которых принимал участие, тк
    // спринг падает на циклическом вызове методов конвертации
}
