package gb.pract.aspect.logging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.slf4j.event.Level;


@Data
@ConfigurationProperties("application.logging") //указываем сам путь к значениям уровня
public class NewLoggingProperties {
//    private LoggingLevel level = LoggingLevel.DEBUG;
    //можно указать вот такое значение по умолчанию, если вдруг в application.yaml это не описано

    //тк Level это уже унам, можно сделать вот так:
    private Level level = Level.DEBUG;
//    так как мы пришли к варианту, что можно выключать на уровне .yaml, тут можно этот параметр убрать
//    private Boolean printArgs = true;
}
