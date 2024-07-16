package gb.pract.timesheet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long projectId;
    private String name;
    @OneToMany
    private List<Timesheet> timesheetList;
    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "employeeId")
    )
    @JsonIgnoreProperties(value = {"timesheetList", "projectList"})
    private List<Employee> employeeList;
}
