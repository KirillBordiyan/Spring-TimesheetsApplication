package gb.pract.aspect.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NewLoggingProperties.class) //для подтягивания значений из проперти указанного класса
// (появляется бин этого класса)
@ConditionalOnProperty(value = "application.logging.enable-aspect", havingValue = "true")
//вот это вариант, чтобы не проверять в самом коде включена ли аннотация или нет
public class NewLoggingAutoConfiguration {

    //прокидываем properties, тк это уже бин со своими значениями
    @Bean
    public NewLoggingAspect newLoggingAspect(NewLoggingProperties properties) {
        return new NewLoggingAspect(properties);
    }
}
