package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.page.TimesheetPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {

    private final TimesheetService timesheetService;
    private final ProjectService projectService;

    public Optional<TimesheetPageDTO> findById(Long id) {
        return timesheetService.findById(id)
                .map(this::convert);
    }

    public List<TimesheetPageDTO> findAll() {
        return timesheetService.findAll(null, null).stream()
                .map(this::convert)
                .toList();
    }

    private TimesheetPageDTO convert(Timesheet timesheet){
        Project project = projectService.getById(timesheet.getProjectId()).orElseThrow();

        TimesheetPageDTO timesheetPageDTO = new TimesheetPageDTO();

        timesheetPageDTO.setId(String.valueOf(timesheet.getId()));
        timesheetPageDTO.setCreatedAt(String.valueOf(timesheet.getCreatedAt()));
        timesheetPageDTO.setMinutes(String.valueOf(timesheet.getMinutes()));
        timesheetPageDTO.setProjectName(project.getName());

        return timesheetPageDTO;
    }
}
