package gb.pract.timesheet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "timesheet")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long projectId; //при удалении будет -1
    private Long employeeId; // тот, кто отработал
    private Integer minutes;
    private LocalDate createdAt;
}
