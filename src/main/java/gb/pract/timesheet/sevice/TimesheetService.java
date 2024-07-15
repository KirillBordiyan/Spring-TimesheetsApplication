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

    public Optional<Timesheet> findById(java.lang.Long id) {
        return timesheetRepository.findById(id);
    }

    //TODO переделать с границами
    public List<Timesheet> findAll() {
        return timesheetRepository.findAll();
    }

    public Timesheet saveTimesheet(Timesheet timesheet) {

        if(Objects.isNull(timesheet.getProjectId())){
            throw new IllegalArgumentException("Project ID must not be null");
        }

        if(projectRepository.findById(timesheet.getProjectId()).isEmpty()){
            throw new NoSuchElementException("Project with ID " + timesheet.getProjectId() + " does not exists");
        }

        timesheet.setCreatedAt(LocalDate.now());
        timesheetRepository.save(timesheet);

        Optional<Project> projectInnerDB = projectRepository.findById(timesheet.getProjectId());

        projectInnerDB.ifPresent(project -> {
            Long projectId = project.getProjectId();
            if(projectRepository.addTimesheet(projectId, timesheet)){
                projectRepository.save(project);
            }
        });

        return timesheet;
    }

    public void delete(java.lang.Long id) {
        timesheetRepository.deleteById(id);
    }
}
