package gb.pract.timesheetRest.aop;

import gb.pract.timesheetRest.aop.myAnno.Timer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class TimerAspect {


    //тут мы учитываем в Pointcut, что сработает, если есть аннотация
    @Pointcut("@annotation(gb.pract.timesheet.aop.myAnno.Timer)")
    public void timerMethodPointcut() {
    }

    //а вот такой вариант поинтката, если аннотация стоит для классов
    @Pointcut("@within(gb.pract.timesheet.aop.myAnno.Timer)")
    public void timerTypePointcut() {
    }

    @Around(value = "timerMethodPointcut() || timerTypePointcut()")
    public Object aroundTimesheetService(ProceedingJoinPoint pjp) throws Throwable {

        boolean anno = isAnnoEnabled(pjp);

        if (!anno) {
            return pjp.proceed();
        }

        long start = System.currentTimeMillis();

        try {
            log.info("Метод {}() был вызван", pjp.getSignature().getName());
            return pjp.proceed();
        } finally {
            Long millis = System.currentTimeMillis() - start;
            log.info("duration -->> {}", millis);
        }
    }

    private static boolean isAnnoEnabled(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object targetClass = pjp.getTarget();
        Method method = signature.getMethod();

        boolean anno = true; // по умолчанию включена
        if (method.isAnnotationPresent(Timer.class)) {
            anno = method.getAnnotation(Timer.class).enabled(); //если представлена над методом - берем оттуда
        } else if (targetClass.getClass().isAnnotationPresent(Timer.class)) {
            anno = targetClass.getClass().getAnnotation(Timer.class).enabled(); // если представлена над классом - берем оттуда
        }
        return anno;
    }

}
