package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project saveProject(Project project) {
        if(Objects.isNull(project.getName())){
            throw new IllegalArgumentException("Project name must not be null");
        }
        project.setTimesheetList(new ArrayList<>());
        projectRepository.save(project);
        return project;
    }

    public void delete(Long id) {
        projectRepository.findAll().stream()
                .filter(project -> Objects.equals(project.getProjectId(), id))
                .forEach(project -> project
                        .getTimesheetList()
                        .forEach(timesheet -> timesheet.setProjectId(-1L)));

        projectRepository.deleteById(id);
    }
}
