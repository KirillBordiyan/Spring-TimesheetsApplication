package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Optional<Project> getById(Long id) {
        return projectRepository.getById(id);
    }

    public List<Project> getAll() {
        return projectRepository.getAll();
    }

    public Project create(Project project) {
        project = projectRepository.create(project);
        return project;
    }

    public void delete(Long id) {
        projectRepository.getAll().stream()
                .filter(project -> Objects.equals(project.getProject_id(), id))
                .forEach(project -> project
                        .getTimesheetList()
                        .forEach(timesheet -> timesheet.setProjectId(-1L)));

        projectRepository.delete(id);
    }
}
