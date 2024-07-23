package gb.pract.timesheet.security;

import gb.pract.timesheet.model.Client;
import gb.pract.timesheet.model.Role;
import gb.pract.timesheet.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Client client = clientRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с login: " + login + " не найден"));

        return new User(
                client.getLogin(),
                client.getPassword(),
                getRole(client)
        );
    }

    private List<SimpleGrantedAuthority> getRole(Client client) {
        List<String> clientRoles = client.getRoles().stream().map(Role::toString).toList();
        return clientRoles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
