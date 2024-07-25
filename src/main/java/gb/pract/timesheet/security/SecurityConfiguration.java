package gb.pract.timesheet.security;

import gb.pract.timesheet.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtFilter;
//    TODO нужен был для обработки запросов с разными входными
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                TODO что-то типа вот так делаются правила для нескольких точек входа
//                .exceptionHandling(handling -> handling
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            if (request.getRequestURI().startsWith("/api")) {
//                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Устанавливаем статус 401
//                                response.getWriter().write("401 unauthorized"); // Отправляем пустую страницу
//                            }else {
//                                response.sendRedirect("/login");
//                            }
//                        })
//                )
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/authenticate", "/timesheets-documentation", "/login").permitAll();
                    request.requestMatchers("/home/projects/**", "/home/employees/**").hasAuthority("admin");
                    request.requestMatchers("/home/timesheets/**").hasAnyAuthority("admin", "user");
                    //TODO тут, тк ссылка на рест не попадает не под один фильтр выше
                    // обновлена ссылка на JSON, тут учтено
                    request.requestMatchers("/api/**").hasAuthority("rest");
                    request.anyRequest().authenticated();
                })
                .formLogin(httpFormLogin -> httpFormLogin
                        //TODO это надо для редиректа при успешной авторизации, расписано это в этом handler'е
                        .successHandler(new AuthenticationSuccessHandler())
                        .permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
