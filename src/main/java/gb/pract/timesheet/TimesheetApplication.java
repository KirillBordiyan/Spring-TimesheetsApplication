package gb.pract.timesheet;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.ProjectRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import gb.pract.timesheet.sevice.ProjectService;
import gb.pract.timesheet.sevice.TimesheetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class TimesheetApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TimesheetApplication.class, args);
//		TimesheetRepository tr = context.getBean(TimesheetRepository.class);
//		ProjectRepository pr = context.getBean(ProjectRepository.class);
		ProjectService ps = context.getBean(ProjectService.class);
		TimesheetService ts = context.getBean(TimesheetService.class);
		Random rnd = new Random();

		for(int i = 1; i <= 3; i++){
			Project project = new Project();
			project.setName("projectName#" + i);
			ps.create(project);
		}

		LocalDate createdAt = LocalDate.now();

		for(int i = 1; i <= 10; i++ ){
			Timesheet timesheet = new Timesheet();
			createdAt = createdAt.plusDays(i);

			timesheet.setCreatedAt(createdAt);
			timesheet.setProjectId(rnd.nextLong(1,4));
			timesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));

			ts.create(timesheet);
		}

	}

}
