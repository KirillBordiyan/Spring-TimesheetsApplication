package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.ProjectRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ProjectRepository projectRepository;

    public Optional<Timesheet> findById(Long id) {
        return timesheetRepository.getById(id);
    }

    public List<Timesheet> findAll(LocalDate createdAtBefore, LocalDate createdAtAfter) {
        return timesheetRepository.findAll(createdAtBefore, createdAtAfter);
    }

    public Timesheet create(Timesheet timesheet) {

        if(Objects.isNull(timesheet.getProjectId())){
            throw new IllegalArgumentException("Project ID must not be null");
        }

        if(projectRepository.findById(timesheet.getProjectId()).isEmpty()){
            throw new NoSuchElementException("Project with ID " + timesheet.getProjectId() + " does not exists");
        }

        timesheet.setCreatedAt(LocalDate.now());
        timesheetRepository.create(timesheet);

        Optional<Project> projectInnerDB = projectRepository.findById(timesheet.getProjectId());

        projectInnerDB.ifPresent(project -> {
            Long projectId = project.getProject_id();
            projectRepository.addTimesheet(projectId, timesheet);
        });

        return timesheet;
    }

    public void delete(Long id) {
        timesheetRepository.delete(id);
    }
}
