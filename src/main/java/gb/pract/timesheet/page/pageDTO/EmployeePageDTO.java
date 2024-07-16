package gb.pract.timesheet.page.pageDTO;

import lombok.Data;

import java.util.List;

@Data
public class EmployeePageDTO {
    private String employeeId;
    private String fullName;
    private String stillWork;
    private List<TimesheetPageDTO> timesheetList;
    //FIXME сотрудник не хранит проекты, в которых принимал участие, тк
    // спринг падает на циклическом вызове методов конвертации
}
