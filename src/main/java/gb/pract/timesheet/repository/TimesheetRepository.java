package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Timesheet;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TimesheetRepository {

    private static Long sequence = 1L;
    private final List<Timesheet> timesheetList = new ArrayList<>();

    public Optional<Timesheet> getById(Long id) {
        return timesheetList.stream()
                .filter(it -> Objects.equals(it.getId(), id))
                .findFirst();
    }

    public List<Timesheet> getAll() {
        return List.copyOf(timesheetList);
    }

    public Timesheet create(Timesheet timesheet) {
        timesheet.setId(sequence++);
        timesheetList.add(timesheet);
        return timesheet;
    }

    public void delete(Long id) {
        timesheetList.stream()
                .filter(it -> Objects.equals(it.getId(), id))
                .findFirst()
                .ifPresent(timesheetList::remove);
    }

    //TODO переписать методы filterAfter/Before и getAll в один
    // в объединенном методе ifы на проверку значений
    // если указано 2 -> делать выборку между
    // 1 ч 9 минут, на моздании ссылки на один лист
    public List<Timesheet> filterAfter(LocalDate filterDate) {
        return List.copyOf(timesheetList.stream()
                .filter(timesheet -> timesheet.getCreatedAt().isAfter(filterDate))
                .toList());
    }

    public List<Timesheet> filterBefore(LocalDate filterDate) {
        return List.copyOf(timesheetList.stream()
                .filter(timesheet -> timesheet.getCreatedAt().isBefore(filterDate))
                .toList());
    }
}
