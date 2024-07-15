package gb.pract.timesheet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long employeeId;
    private String fullName;
    private Boolean stillWork;
    //TODO аннотации transient(тк не по объекту целиком, а только id)
    @OneToMany
    private List<Timesheet> timesheetList;
    //todo manytomany
    @ManyToMany(mappedBy = "employeeList")
    private List<Project> projectList;

}
