package gb.pract.timesheetRest.repository;

import gb.pract.timesheetRest.model.Project;
import gb.pract.timesheetRest.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Deprecated
    default boolean addTimesheet(Long projectId, Timesheet timesheet) {
        Project project = this.getReferenceById(projectId);
        return project.getTimesheetList().add(timesheet);
    }

    default <T> void addProjectParam(Long projectId, T object, Function<Project, List<T>> getListFunction) {
        Project project = this.getReferenceById(projectId);
        getListFunction.apply(project).add(object);
    }
}
