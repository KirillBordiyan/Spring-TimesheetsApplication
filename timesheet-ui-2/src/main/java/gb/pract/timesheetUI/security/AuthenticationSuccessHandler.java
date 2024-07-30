package gb.pract.timesheetUI.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("admin"));
        boolean isRest = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("rest"));
        if (isAdmin) {
            //если админ - проекты
            setDefaultTargetUrl("/home/projects");
        } else if (isRest){
            // если рестовый - на JSON
            setDefaultTargetUrl("/api/timesheets");
        } else {
            //если пользователь - таймшиты
            setDefaultTargetUrl("/home/timesheets");
        }
        //TODO если рест - рест-ресурсы, но только при условии, что он авторизован (если нет, то 401 инстантно)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
