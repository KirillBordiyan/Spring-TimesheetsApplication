package gb.pract.timesheetRest.repository;

import gb.pract.timesheetRest.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    List<Timesheet> findByCreatedAtBetween(LocalDate createdBefore, LocalDate createdAfter);
    List<Timesheet> findByCreatedAtAfter(LocalDate createdAfter);
    List<Timesheet> findByCreatedAtBefore(LocalDate createdBefore);
}
