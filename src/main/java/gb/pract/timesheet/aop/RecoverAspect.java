package gb.pract.timesheet.aop;

import gb.pract.timesheet.aop.myAnno.Recover;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
@Aspect
public class RecoverAspect {


    @Pointcut("@annotation(gb.pract.timesheet.aop.myAnno.Recover)")
    public void recoverMethodPointcut() {
    }

    @AfterThrowing(value = "recoverMethodPointcut()", throwing = "exc")
    public Object afterThrowingRecoverAnnotation(JoinPoint jp, Exception exc) throws Throwable {

        Method method = ((MethodSignature) jp.getSignature()).getMethod();

        //FIXME реализация и возвраты как по заданию (вроде), но из-за того, что у нас есть
        //  GlobalExceptionHandler, где обработка исключений немного иная (в плане, всегда возвращаем ExceptionResponse)
        //  то как по мне лучше в каких-то конкретных ситуациях выбрасывать RuntimeException с разными сообщениями
        //  чтобы видеть различия получаемых сообщений (примерно как комменты ниже) -> так более понятно, наверное
        //  (если не делать здесь throw new Runtime(), то будет всегда сообщение из пришедшего Exc)
        //
        if (method.isAnnotationPresent(Recover.class)) { //если представлена
            Class<?>[] ignoredClasses = method.getAnnotation(Recover.class).noRecovered();
//            если метод void, можно прописать явно, но возврат будет как при ссылочном
//            if(Void.class.equals(method.getReturnType())){
//                return null;
//            }
            //если примитивный тип
            log.error("Исключение -->> {}", exc.getClass().getName());
            log.error("-->> {}", exc.getMessage()); //оригинальное сообщение, увидим в консоли
            if (Arrays.asList(ignoredClasses).contains(exc.getClass())) {
                throw new RuntimeException("Исключение " + exc.getClass().getName() + " не обрабатывается");
            }
            if (method.getReturnType().isPrimitive()) {
                Class<?> type = method.getReturnType();
                Object defaultValue = getDefaultValue(type);
                log.error("return default primitive value ->> {}", defaultValue);
                return defaultValue;
            }
            log.error("return null");
//            throw new RuntimeException("Исключение, которое вернет null");  //увидим в постмане, если раскоментим
            return null;
        } else {
//            это чисто ради сообщения другого
//            throw new RuntimeException("Исключение, когда аннотации @Recover не было");
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
