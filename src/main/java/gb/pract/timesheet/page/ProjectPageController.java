package gb.pract.timesheet.page;

import gb.pract.timesheet.page.pageDTO.ProjectPageDTO;
import gb.pract.timesheet.sevice.page.ProjectPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/home/projects")
@RequiredArgsConstructor
public class ProjectPageController {

    private final ProjectPageService projectpageService;

    @GetMapping
    public String getAllProjects(Model model) {
        List<ProjectPageDTO> projects = projectpageService.findAll();
        model.addAttribute("projects", projects);
        return "projects-page.html";
    }

    @GetMapping("/{id}")
    public String getProjectPage(@PathVariable Long id, Model model) {

        Optional<ProjectPageDTO> pageDTO = projectpageService.findById(id);
        if (pageDTO.isEmpty()) {
            throw new NoSuchElementException();
        }

        model.addAttribute("projectTimesheets", pageDTO.get().getTimesheetList());
        model.addAttribute("projectEmployees", pageDTO.get().getEmployeeList());
        model.addAttribute("project", pageDTO.get());
        return "project-page.html";
    }

}
