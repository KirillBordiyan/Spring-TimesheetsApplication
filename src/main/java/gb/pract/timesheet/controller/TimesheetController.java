package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Descriptor;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
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
    public ResponseEntity<List<Timesheet>> findAll(@RequestParam(required = false, name = "createdAfter")
                                                   LocalDate createdAtAfter,
                                                   @RequestParam(required = false, name = "createdBefore")
                                                   LocalDate createdAtBefore) {
        return ResponseEntity.ok(timesheetService.findAll(createdAtAfter, createdAtBefore));
        //FIXME при передаче null в url будет MethodArgumentTypeMismatchException
        // если ?createdBefore=..?created будет ошибка
    }

    @PostMapping
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheet) {
        timesheet = timesheetService.saveTimesheet(timesheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timesheetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
