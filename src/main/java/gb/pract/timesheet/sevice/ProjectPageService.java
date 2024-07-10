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

    public Optional<ProjectPageDTO> findById(Long id) {
        return projectService.getById(id)
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

        projectDTO.setId(String.valueOf(project.getProject_id()));
        projectDTO.setName(String.valueOf(project.getName()));
        projectDTO.setTimesheets(project.getTimesheetList().stream()
                .map(timesheetPageService::convertTimesheet)
                .toList());

        return projectDTO;
    }
}
