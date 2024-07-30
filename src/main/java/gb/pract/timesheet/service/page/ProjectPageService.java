package gb.pract.timesheetRest.service.page;

import gb.pract.timesheetRest.model.Project;
import gb.pract.timesheetRest.pageDTO.ProjectPageDTO;
import gb.pract.timesheetRest.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPageService {

    private final ProjectService projectService;
    private final TimesheetPageService timesheetPageService;
    private final EmployeePageService employeePageService;

    public Optional<ProjectPageDTO> findById(Long id) {
        return projectService.findById(id)
                .map(this::convertProject);
    }

    public List<ProjectPageDTO> findAll() {
        return projectService.findAll()
                .stream()
                .map(this::convertProject)
                .toList();
    }

    public ProjectPageDTO convertProject(Project project) {

        ProjectPageDTO projectDTO = new ProjectPageDTO();

        projectDTO.setId(String.valueOf(project.getProjectId()));
        projectDTO.setName(String.valueOf(project.getName()));
        projectDTO.setTimesheetList(project.getTimesheetList().stream()
                .map(timesheetPageService::convertTimesheet)
                .toList());
        projectDTO.setEmployeeList(project.getEmployeeList().stream()
                .map(employeePageService::convertEmployee)
                .toList());

        return projectDTO;
    }
}
