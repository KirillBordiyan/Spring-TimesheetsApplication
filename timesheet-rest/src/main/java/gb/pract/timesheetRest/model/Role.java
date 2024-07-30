package gb.pract.timesheetRest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long roleId;
    private String roleName;

    @Override
    public String toString() {
        return this.getRoleName();
    }
}
