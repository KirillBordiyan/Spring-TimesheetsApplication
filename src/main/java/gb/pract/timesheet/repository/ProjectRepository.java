package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    default boolean addTimesheet(Long projectId, Timesheet timesheet) {
        Project project = this.getReferenceById(projectId);
        return project.getTimesheetList().add(timesheet);
    }
}
