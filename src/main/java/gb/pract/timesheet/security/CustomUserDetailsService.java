package gb.pract.timesheet.security;

import gb.pract.timesheet.model.Client;
import gb.pract.timesheet.model.depracated.RoleEnumDeprecated;
import gb.pract.timesheet.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Client client = clientRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с login: " + login + " не найден"));

        return User.builder()
                .username(client.getLogin())
                .password(client.getPassword())
                .roles(getRole(client))
                .build();
    }

    private String[] getRole(Client client) {
        if (client.getRoles() == null) {
            return new String[]{RoleEnumDeprecated.USER.getRole()};
        }
        return client.getRoles().toString().split(", ");
    }
}
