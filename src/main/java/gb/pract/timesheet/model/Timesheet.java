package gb.pract.timesheet.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Timesheet {

    private Long id;
    private Long projectId; //при удалении будет -1
    private LocalDate createdAt;
    private Integer minutes;
}
