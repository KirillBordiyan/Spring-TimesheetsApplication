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
        Optional<Timesheet> timesheet = timesheetService.getById(id);
        return timesheet.map(value -> ResponseEntity
                        .status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    //TODO переписать методы filterAfter/Before и getAll в один
    // тут вызывается только 1 метод сервиса
    @GetMapping
    public ResponseEntity<List<Timesheet>> getCreatedAt(@RequestParam(required = false) LocalDate createdAtAfter,
                                                        @RequestParam(required = false) LocalDate createdAtBefore) {
        if (createdAtAfter != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(timesheetService.filterAfter(createdAtAfter));

        } else if (createdAtBefore != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(timesheetService.filterBefore(createdAtBefore));
        }
        return getAll();
    }

    @PostMapping
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
        timesheet = timesheetService.create(timesheet);
        if (timesheet == null) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timesheetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<Timesheet>> getAll() {
        List<Timesheet> resultList = timesheetService.getAll();
        if (resultList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(timesheetService.getAll());
    }
}
