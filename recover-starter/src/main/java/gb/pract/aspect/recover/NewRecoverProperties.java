package gb.pract.aspect.recover;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("application.recover")
public class NewRecoverProperties {

    private Boolean enabled = true;
    private List<Class<?>> noRecoverFor = new ArrayList<>();
}
