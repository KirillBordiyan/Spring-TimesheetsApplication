package gb.pract.timesheetRest;

import gb.pract.aspect.logging.NewLoggingAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient //для eureka, было EnableEurekaClient
@SpringBootApplication
//@Import(NewLoggingAutoConfiguration.class) //если конфиг находится внутри самого модуля
public class TimesheetRestApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TimesheetRestApplication.class, args);
	}
}
