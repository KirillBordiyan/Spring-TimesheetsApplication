package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.page.pageDTO.TimesheetPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {

    private final TimesheetService timesheetService;
    private final ProjectService projectService;

    public Optional<TimesheetPageDTO> findById(java.lang.Long id) {
        return timesheetService.findById(id)
                .map(this::convertTimesheet);
    }

    public List<TimesheetPageDTO> findAll() {
        return timesheetService.findAll().stream()
                .map(this::convertTimesheet)
                .toList();
    }

    public TimesheetPageDTO convertTimesheet(Timesheet timesheet) {
        Optional<Project> projectInnerDB = projectService.findById(timesheet.getProjectId());

        TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();

        timesheetPageDTO.setId(String.valueOf(timesheet.getId()));
        timesheetPageDTO.setCreatedAt(String.valueOf(timesheet.getCreatedAt()));
        timesheetPageDTO.setMinutes(String.valueOf(timesheet.getMinutes()));

        projectInnerDB.ifPresent(project -> {
            timesheetPageDTO.setProjectId(String.valueOf(project.getProjectId()));
            timesheetPageDTO.setProjectName(project.getName());
        });

        return timesheetPageDTO;
    }
}
