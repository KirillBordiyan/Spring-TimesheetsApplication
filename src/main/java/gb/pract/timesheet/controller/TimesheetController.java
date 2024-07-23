package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.sevice.TimesheetService;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
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
@RequestMapping("/api/timesheets")
@RequiredArgsConstructor
@Tag(name = "Timesheet", description = "Для взаимодействия с Timesheet")
public class TimesheetController {

    private final TimesheetService timesheetService;

    @Operation(
            summary = "Get timesheet by id",
            description = "Метод получения записи времени по id",
            tags = {"Get", "Timesheet"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Timesheet.class))),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Некорректный запрос",
//                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Контент не найден",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Timesheet> get(@PathVariable @Parameter(description = "Id таймшита") Long id) {
        Optional<Timesheet> timesheet = timesheetService.findById(id);
        return timesheet.map(value -> ResponseEntity
                        .status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(
            summary = "Get all timesheets",
            description = "Метод получения всего списка записей, можно фильтровать по датам",
            tags = {"Get", "Timesheet"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неправильный параметр запроса",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            }
    )
    @GetMapping
    public ResponseEntity<List<Timesheet>> findAll(
            @Parameter(description = "Дата ПОСЛЕ которой создана запись")
                @RequestParam(required = false, name = "createdAfter")
                LocalDate createdAtAfter,
            @Parameter(description = "Дата ДО которой создана запись")
                @RequestParam(required = false, name = "createdBefore")
                LocalDate createdAtBefore) {
        return ResponseEntity.ok(timesheetService.findAll(createdAtAfter, createdAtBefore));
        //TODO при передаче null в url будет MethodArgumentTypeMismatchException
        // если ?createdBefore=..?created будет ошибка
    }

    @Operation(
            summary = "Post timesheet",
            description = "Метод для публикации записи рабочего времени",
            tags = {"Post", "Timesheet"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Создана запись",
                            content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID проекта или сотрудника null",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Не внесен ID проекта или сотрудника",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<Timesheet> create(@Parameter(description ="Запись с минимальными требуемыми значениями")
                                                @RequestBody Timesheet timesheet) {
        timesheet = timesheetService.saveTimesheet(timesheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);
    }

    @Operation(
            summary = "Delete timesheet",
            description = "Метод для удаления записи времени",
            tags = {"Delete", "Timesheet"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Запись успешно удалена, либо такой записи не было",
                            content = @Content(schema = @Schema(implementation = Timesheet.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Id для удаления записи") @PathVariable Long id) {
        timesheetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
