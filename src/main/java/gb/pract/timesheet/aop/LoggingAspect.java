package gb.pract.timesheet.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    //Before
    //After
    //AfterThrowing
    //AfterReturning
    //Around -> AfterReturning + AfterThrowing
    //execution(
    // {типы методов (* если все)}
    // {тип возвращаемого (* если все)}
    //      {
    //          путь к папке от корня.метод(* если метод любой)({тип параметра, который требуют метод/ы})
    //      }
    // )

    // тут представлены случаи, когда это срабатывает по ситуации независимо от аннотаций

    @Pointcut("execution(* gb.pract.timesheet.service.TimesheetService.*(..))")
    public void timesheetServiceMethodPointcut() {
    }

    @Pointcut("execution(* gb.pract.timesheet.service.ProjectService.*(..))")
    public void ProjectServiceMethodPointcut() {
    }

    @Pointcut("execution(* gb.pract.timesheet.service.EmployeeService.*(..))")
    public void employeeServiceMethodPointcut() {
    }

    @Before(value = "EmployeeServiceMethodPointcut()")
    public void beforeEmployeeService(JoinPoint jp) {

        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        String[] argsNames = ((MethodSignature) jp.getSignature()).getParameterNames();
        Class<?>[] paramTypes = ((MethodSignature) jp.getSignature()).getParameterTypes();
        Map<String, Map<String, String>> map = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Map<String, String> varMap = new HashMap<>();
            String typeName = paramTypes[i].getSimpleName();
            String name = "";
            String varValue = "";
            for (int j = 0; j < argsNames.length; j++) {
                name = argsNames[j];
                varValue = args[j].toString();
                varMap.put(name, varValue);
            }
            map.put(typeName, varMap);
        }

        if (method.equals("saveEmployee")) {
            return;
        }

        log.info("LOGGING Before EmployeeService.{}() with params:-->> ", method);
        Stream.of(map.entrySet()).forEach(el -> el.forEach(
                LoggingAspect::getInfo)
        );
    }

    @Before(value = "ProjectServiceMethodPointcut()")
    public void beforeProjectService(JoinPoint jp) {

        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        String[] argsNames = ((MethodSignature) jp.getSignature()).getParameterNames();
        Class<?>[] paramTypes = ((MethodSignature) jp.getSignature()).getParameterTypes();
        Map<String, Map<String, String>> map = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Map<String, String> varMap = new HashMap<>();
            String typeName = paramTypes[i].getSimpleName();
            String name = "";
            String varValue = "";
            for (int j = 0; j < argsNames.length; j++) {
                name = argsNames[j];
                varValue = args[j].toString();
                varMap.put(name, varValue);
            }
            map.put(typeName, varMap);
        }

        if (method.equals("saveProject")) {
            return;
        }

        log.info("LOGGING Before ProjectService.{}() with params:-->> ", method);
        Stream.of(map.entrySet()).forEach(el -> el.forEach(
                LoggingAspect::getInfo)
        );
    }

    @Before(value = "timesheetServiceMethodPointcut()")
    public void beforeTimesheetService(JoinPoint jp) {

        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        String[] argsNames = ((MethodSignature) jp.getSignature()).getParameterNames();
        Class<?>[] paramTypes = ((MethodSignature) jp.getSignature()).getParameterTypes(); //нашел в тырнетах, вычленяет классы
        Map<String, Map<String, String>> map = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Map<String, String> varMap = new HashMap<>(); //внутренний список
            String typeName = paramTypes[i].getSimpleName();//имя типа
            String name = "";
            String varValue = "";
            for (int j = 0; j < argsNames.length; j++) {
                name = argsNames[j]; //имя передаваемого параметра (id, createdAt, ...)
                varValue = args[j].toString(); // само значение
                varMap.put(name, varValue);
            }
            map.put(typeName, varMap);
        }

        if (method.equals("saveTimesheet")) {
            return;
        }

        //FIXME вроде бы по идее должно работать, при условии что передается несколько параметров разных типов
        // делал как мапа-в-мапе, да, можно было сделать каждый раз новая строка, но не знаю, ситуативно
        // мне больше нравится так, тут и тип, и название, и само значение
        log.info("LOGGING Before TimesheetService.{}() with params:-->> ", method);
        Stream.of(map.entrySet()).forEach(el -> el.forEach(
                LoggingAspect::getInfo)
        );
    }

    private static void getInfo(Map.Entry<String, Map<String, String>> inner) {
        log.info("-->> Type: {}, Val: {}", inner.getKey(), inner.getValue().entrySet());
    }

    // или в value = "timesheetServicePointcut()"
    @After(value = "execution(* gb.pract.timesheet.service.TimesheetService.save*(Timesheet))")
    public void afterTimesheetService(JoinPoint jp) {
        String method = jp.getSignature().getName();
        log.info("LOGGING After save -->> TimesheetService -->> {}();", method);
    }

//    @AfterThrowing(value = "timesheetServiceMethodPointcut()", throwing = "ex")
//    public void exceptionTimesheetServiceFindById(JoinPoint jp, Exception ex){
//        String method = jp.getSignature().getName();
//        log.error("LOGGING AfterThrowing -- ServiceMethod -->> {}();\nexception -->> {}", method, ex.getClass().getName());
//    }
}
