package gb.pract.timesheet.sevice;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.repository.ProjectRepository;
import gb.pract.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ProjectRepository projectRepository;

    public Optional<Timesheet> getById(Long id) {
        return timesheetRepository.getById(id);
    }

    public List<Timesheet> getAll() {
        return timesheetRepository.getAll();
    }

    public Timesheet create(Timesheet timesheet) {
        if (!isProjectExist(timesheet.getProjectId())) {
            return null;
        }
        timesheet.setCreatedAt(LocalDate.now());
        timesheetRepository.create(timesheet);

        Optional<Project> projectInnerDB = projectRepository.getById(timesheet.getProjectId());

        projectInnerDB.ifPresent(project -> {
            Long projectId = project.getProject_id();
            projectRepository.addTimesheet(projectId, timesheet);
        });

        return timesheet;
    }

    public void delete(Long id) {
        timesheetRepository.delete(id);
    }

    //TODO переписать методы filterAfter/Before и getAll в один
    // тут есть 1 метод и он вызывает метод в репозитории
    public List<Timesheet> filterAfter(LocalDate filterDate) {
        return timesheetRepository.filterAfter(filterDate);
    }

    public List<Timesheet> filterBefore(LocalDate filterDate) {
        return timesheetRepository.filterBefore(filterDate);
    }

    private boolean isProjectExist(Long projectId) {
        return projectRepository.getAll()
                .stream()
                .anyMatch(project -> Objects.equals(project.getProject_id(), projectId));
    }
}
