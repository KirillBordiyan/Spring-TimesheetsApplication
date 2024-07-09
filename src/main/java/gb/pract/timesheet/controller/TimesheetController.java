package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;

    @GetMapping("/{id}")
    public ResponseEntity<Timesheet> get(@PathVariable Long id) {
        Optional<Timesheet> timesheet = timesheetService.findById(id);
        return timesheet.map(value -> ResponseEntity
                        .status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<Timesheet>> findAll(@RequestParam(required = false) LocalDate createdAtAfter,
                                                   @RequestParam(required = false) LocalDate createdAtBefore) {
        return ResponseEntity.ok(timesheetService.findAll(createdAtBefore, createdAtAfter));
    }

    @PostMapping
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
        timesheet = timesheetService.create(timesheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timesheetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
