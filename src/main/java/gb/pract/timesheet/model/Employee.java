package gb.pract.timesheet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
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
    @OneToMany
    private List<Timesheet> timesheetList;
    @ManyToMany(mappedBy = "employeeList")
    @JsonIgnoreProperties(value = {"timesheetList", "employeeList"})
    private List<Project> projectList;
}
