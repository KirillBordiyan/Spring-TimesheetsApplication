package gb.pract.timesheet.repository;

import gb.pract.timesheet.model.Timesheet;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public class TimesheetRepository {

    private static Long sequence = 1L;
    private final List<Timesheet> timesheetList = new ArrayList<>();

    public Optional<Timesheet> getById(Long id) {
        return timesheetList.stream()
                .filter(it -> Objects.equals(it.getId(), id))
                .findFirst();
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

    public List<Timesheet> findAll(LocalDate createdAtBefore, LocalDate createdAtAfter) {
        Predicate<Timesheet> filter = it -> true;

        if (Objects.nonNull(createdAtBefore)) {
            filter = filter.and(it -> it.getCreatedAt().isBefore(createdAtBefore));
        }

        if (Objects.nonNull(createdAtAfter)) {
            filter = filter.and(it -> it.getCreatedAt().isAfter(createdAtAfter));
        }

        return timesheetList.stream()
                .filter(filter)
                .toList();
    }
}
