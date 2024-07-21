package gb.pract.timesheet.model;

import gb.pract.timesheet.model.depracated.RoleEnumDeprecated;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long clientId;
    private String login;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_role",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * @Deprecated
     * эта часть сделана как вариант, если мы делаем роли как Enum
     * в настоящий момент реализация сделана через POJO класс ролей
     * вернуть = раскоментить и убрать класс Role
     * @ElementCollection(targetClass = RoleEnumDeprecated.class, fetch = FetchType.EAGER)
     * @CollectionTable(name = "client_roles", joinColumns = @JoinColumn(name = "client_id"))
     * @Enumerated(EnumType.STRING)
     * private Set<RoleEnumDeprecated> roles;
*/
}
