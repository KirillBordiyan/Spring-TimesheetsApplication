package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProjectRepository {

    private static Long sequence = 1L;
    private final List<Project> projectList = new ArrayList<>();

    public Optional<Project> findById(Long id) {
        return projectList.stream()
                .filter(project -> Objects.equals(project.getProject_id(), id))
                .findFirst();
    }

    public List<Project> findAll() {
        return List.copyOf(projectList);
    }

    public Project create(Project project) {
        Optional<Project> projectInnerDB = projectList.stream()
                .filter(el -> Objects.equals(project.getProject_id(), el.getProject_id()))
                .findFirst();

        if (projectInnerDB.isPresent()) {
            return projectInnerDB.get();
        }

        project.setProject_id(sequence++);
        project.setTimesheetList(new ArrayList<>());
        projectList.add(project);
        return project;
    }

    public void delete(Long id) {
        projectList.stream()
                .filter(project -> Objects.equals(project.getProject_id(), id))
                .findFirst()
                .ifPresent(projectList::remove);
    }

    public void addTimesheet(Long projectId, Timesheet timesheet) {
        projectList.get(projectId.intValue() - 1).getTimesheetList().add(timesheet);
    }
}
