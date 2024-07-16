package gb.pract.timesheet.page;


import gb.pract.timesheet.page.pageDTO.TimesheetPageDTO;
import gb.pract.timesheet.sevice.TimesheetPageService;
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
@RequestMapping("/home/timesheets")
@RequiredArgsConstructor
public class TimesheetPageController {

    private final TimesheetPageService timesheetPageService;

    @GetMapping
    public String getAllTimesheets(Model model) {
        List<TimesheetPageDTO> timesheets = timesheetPageService.findAll();
        model.addAttribute("timesheets", timesheets);
        return "timesheets-page.html";
    }

    @GetMapping("/{id}")
    public String getTimesheetsPage(@PathVariable Long id, Model model) {

        Optional<TimesheetPageDTO> pageDTO = timesheetPageService.findById(id);
        if (pageDTO.isEmpty()) {
            throw new NoSuchElementException();
        }

        model.addAttribute("timesheet", pageDTO.get());
        return "timesheet-page.html";
    }

}
