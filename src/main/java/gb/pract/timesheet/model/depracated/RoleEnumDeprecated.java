package gb.pract.timesheet.model.depracated;

import lombok.Getter;

@Getter
public enum RoleEnumDeprecated {

    ADMIN("admin"),
    USER("user");

    public final String role;
    RoleEnumDeprecated(String role) {
        this.role = role;
    }
}
