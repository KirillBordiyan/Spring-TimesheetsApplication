package gb.pract.timesheetRest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@EnableDiscoveryClient //для eureka, было EnableEurekaClient
@SpringBootApplication
public class TimesheetRestApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TimesheetRestApplication.class, args);
	}
}
