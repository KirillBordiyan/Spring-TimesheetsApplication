package gb.pract.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class NewLoggingAspect {

    //инжектим для значений уровня
    private final NewLoggingProperties properties;

    @Pointcut("@annotation(gb.pract.aspect.logging.Logging)")
    public void methodServicePointcut() {
    }

    @Pointcut("@within(gb.pract.aspect.logging.Logging)")
    public void typeServicePointcut() {
    }

    @Around(value = "methodServicePointcut() || typeServicePointcut()")
    public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {

        //если в .yaml поставлена галочка, что он выключен
        //это убирается, так как есть аннотация @ConditionalOnProperty(..)
        /*if(!properties.getPrintArgs()){
            log.atLevel(properties.getLevel()).log("Лог, когда аргументы не печатаются");
            return pjp.proceed();
        }*/

        //если в .yaml все true, то будем проверять, нужно ли логировать аргументы в местах, где стоит аннотация
        boolean anno = isAnnoEnabled(pjp);

        String method = pjp.getSignature().getName();

        if (!anno) {
            try {
                return pjp.proceed();
            } finally {
                log.atLevel(properties.getLevel()).log("ANNOTATION OF NEW LOGGING -->>");
                log.atLevel(properties.getLevel()).log("PRINT ARGS AS PARAM ARE DISABLED -->>");
            }
        } // разделять обязательно
        if (method.equals("saveTimesheet")) {
            return pjp.proceed();
        }

        log.atLevel(properties.getLevel()).log("Before NEW LOGGING -->>");

        Object[] args = pjp.getArgs();
        String[] argsNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        Class<?>[] paramTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        Map<String, Map<String, String>> map = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Map<String, String> varMap = new HashMap<>();
            String typeName = paramTypes[i].getSimpleName();
            String name = "";
            String varValue = "";
            for (int j = 0; j < argsNames.length; j++) {
                name = argsNames[j];
                varValue = String.valueOf(args[j]);
                varMap.put(name, varValue);
            }
            map.put(typeName, varMap);
        }

        try {
            return pjp.proceed();
        } finally {
            log.atLevel(properties.getLevel()).log(
                    "After NEW LOGGING {}.{}() with params:-->> ",
                    pjp.getTarget().getClass().getSimpleName(),
                    method);
            Stream.of(map.entrySet()).forEach(el -> el.forEach(
                    NewLoggingAspect::getInfo)
            );
        }
    }

    private static void getInfo(Map.Entry<String, Map<String, String>> inner) {
        log.info("-->> Type: {}, Val: {}", inner.getKey(), inner.getValue().entrySet());
    }

    //Нужен только для проверки, внутри аннотации
    private static boolean isAnnoEnabled(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object targetClass = pjp.getTarget();
        Method method = signature.getMethod();

        boolean anno = true; // по умолчанию включена, можно null, тк в аннотации по дефолту true
        if (method.isAnnotationPresent(Logging.class)) {
            anno = method.getAnnotation(Logging.class).printArgsInside();
        } else if (targetClass.getClass().isAnnotationPresent(Logging.class)) {
            anno = targetClass.getClass().getAnnotation(Logging.class).printArgsInside();
        }
        return anno;
    }
}
