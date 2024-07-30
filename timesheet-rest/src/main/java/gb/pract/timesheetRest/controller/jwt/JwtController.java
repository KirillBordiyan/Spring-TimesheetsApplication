package gb.pract.timesheetRest.controller.jwt;

import gb.pract.timesheetRest.controller.ExceptionResponse;
import gb.pract.timesheetRest.security.CustomUserDetailsService;
import gb.pract.timesheetRest.security.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService detailsService;

    //TODO этим запросом мы получаем валидный токен пользователя, чтобы не аутентифицироваться каждый раз
    // с параметром VALIDATE он будет жить 30минут
    // на руках этот токен нужен, чтобы корректно отправлять запросы через постман
    // (можно конечно выбрать "Basic Auth" и ввести логин пароль вручную, но все же, токен руки чешутся)
    @PostMapping("/authenticate")
    @Operation(
            description = "Метод отправки запроса на получение токена",
            tags = {"Get"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Токен создан успешно",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Что-то не так с переданными параметрами, токен не может быть создан",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
                    )
            }
    )
    public String authenticateAndGetToken(@Parameter(description = "JSON с login+password для получение токена на основе того, что найдем в БД")
                                          @RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.login(), loginForm.password()
                ));

        if (authentication.isAuthenticated()) {
            return jwtService.generatedToken(detailsService.loadUserByUsername(loginForm.login()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }


}
