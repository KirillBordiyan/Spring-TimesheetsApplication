package gb.pract.timesheetRest.page;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

//@Controller
@ControllerAdvice(basePackageClasses = PageExceptionController.class)
public class PageExceptionController {

//    @GetMapping("/home/oops")
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String oopsRedirect() {
//        return "oops.html";
//    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        return "not-found.html";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "not-found.html";
        //TODO вернуть комменты, если нужно будет оставить редирект
        // пока повторение нормально, не обращать внимания
//        return "redirect:/home/oops";
    }
}
