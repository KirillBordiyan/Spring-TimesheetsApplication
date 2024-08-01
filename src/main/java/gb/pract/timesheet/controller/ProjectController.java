package gb.pract.timesheet.controller;

import gb.pract.timesheet.model.Project;
import gb.pract.timesheet.model.Timesheet;
import gb.pract.timesheet.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "Для взаимодействия с Project")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Get all projects",
            description = "Метод получения всего списка проектов",
            tags = {"Get", "Project"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Project.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @Operation(
            summary = "Get timesheets from project by projectID",
            description = "Метод получения списка записей рабочего времени у конкретного проекта",
            tags = {"Get", "Project"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Project.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект с таким ID не найден",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}/timesheets")
    public ResponseEntity<List<Timesheet>> findTimesheetsByProjectId(@Parameter(description = "ID проекта") @PathVariable Long id) {
        Optional<Project> project = projectService.findById(id);
        return project.map(el -> ResponseEntity.status(HttpStatus.OK).body(el.getTimesheetList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get project by id",
            description = "Метод получения проекта по id",
            tags = {"Get", "Project"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = Project.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Проект с таким ID не найден",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Project> findProjectById(@Parameter(description = "ID проекта") @PathVariable Long id) {
        Optional<Project> project = projectService.findById(id);
        return project.map(el -> ResponseEntity
                        .status(HttpStatus.OK).body(el))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(
            summary = "Post project",
            description = "Метод для публикации проекта",
            tags = {"Post", "Project"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Создана запись",
                            content = @Content(schema = @Schema(implementation = Project.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса (тело проекта)",
                            content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        project = projectService.saveProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @Operation(
            summary = "Delete project",
            description = "Метод для удаления проекта",
            tags = {"Delete", "Project"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Запись успешно удалена, либо такой записи не было",
                            content = @Content(schema = @Schema(implementation = Project.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID для удаления проекта") @PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
