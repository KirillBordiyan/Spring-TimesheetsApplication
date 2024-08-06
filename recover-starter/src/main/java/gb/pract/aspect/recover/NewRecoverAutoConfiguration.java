package gb.pract.aspect.recover;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NewRecoverProperties.class)
@ConditionalOnProperty(value = "application.recover.enabled", havingValue = "true")
public class NewRecoverAutoConfiguration {

    @Bean
    public NewRecoverAspect newRecoverAspect(NewRecoverProperties properties){
        return new NewRecoverAspect(properties);
    }
}
