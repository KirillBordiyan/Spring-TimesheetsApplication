package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.page.ProjectPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPageService {

    private final ProjectService projectService;
    private final TimesheetPageService timesheetPageService;

    public Optional<ProjectPageDTO> findById(java.lang.Long id) {
        return projectService.findById(id)
                .map(this::convertProject);
    }

    public List<ProjectPageDTO> findAll() {
        return projectService.findAll()
                .stream()
                .map(this::convertProject)
                .toList();
    }

    private ProjectPageDTO convertProject(Project project) {

        ProjectPageDTO projectDTO = new ProjectPageDTO();

        projectDTO.setId(String.valueOf(project.getProjectId()));
        projectDTO.setName(String.valueOf(project.getName()));
        projectDTO.setTimesheets(project.getTimesheetList().stream()
                .map(timesheetPageService::convertTimesheet)
                .toList());

        return projectDTO;
    }
}
