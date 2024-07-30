package gb.pract.timesheetUI.restClient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimesheetResponse {
    private Long id;
    private Long projectId; //при удалении будет -1
    private Long employeeId; // тот, кто отработал
    private Integer minutes;
    private LocalDate createdAt;
}
