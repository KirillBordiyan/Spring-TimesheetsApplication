package gb.pract.aspect.recover;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class NewRecoverAspect {

    private final NewRecoverProperties properties;

    @Pointcut("@annotation(gb.pract.aspect.recover.Recover)")
    public void recoverMethodPointcut() {
    }

    @AfterThrowing(value = "recoverMethodPointcut()", throwing = "exc")
    public Object afterThrowingRecoverAnnotation(JoinPoint jp, Exception exc) throws Throwable {

        Method method = ((MethodSignature) jp.getSignature()).getMethod();

        if (method.isAnnotationPresent(Recover.class)) { //если представлена
            List<Class<?>> noRecoverFor = properties.getNoRecoverFor(); //из настойки .yaml
            List<Class<?>> ignoredClasses = Arrays.stream(method.getAnnotation(Recover.class).noRecovered()).toList();
            Set<Class<?>> resultList = new HashSet<>(noRecoverFor);

            boolean replaceList = method.getAnnotation(Recover.class).replaceDefaultList();
            if (!replaceList) {
                resultList.addAll(ignoredClasses);
            }

//              FIXME этот вывод чисто для галочки, чтобы посмотреть результирующий вариант спика классов
//            resultList.forEach(el -> System.out.println(el.getName()));

            log.error("Исключение -->> {}", exc.getClass().getName());
            log.error("-->> {}", exc.getMessage()); //оригинальное сообщение, увидим в консоли
            if (resultList.contains(exc.getClass())) {
                throw new RuntimeException("Исключение " + exc.getClass().getName() + " не обрабатывается");
            }
            if (method.getReturnType().isPrimitive()) {

                Class<?> type = method.getReturnType();
                Object defaultValue = getDefaultValue(type);
                log.error("return default primitive value ->> {}", defaultValue);

                return defaultValue;
            }

            log.error("return null");
            return null;
        } else {
            throw exc;
        }
    }

    private Object getDefaultValue(Class<?> returnType) {
        if (returnType == boolean.class) return false;
        if (returnType == char.class) return '\u0000';
        if (returnType == byte.class) return (byte) 0;
        if (returnType == short.class) return (short) 0;
        if (returnType == int.class) return 0;
        if (returnType == long.class) return 0L;
        if (returnType == float.class) return 0.0f;
        if (returnType == double.class) return 0.0d;
        return null;
    }
}
