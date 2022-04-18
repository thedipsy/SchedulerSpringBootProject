package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/desks")
public class DeskController {

    private final OfficeService officeService;
    private final DeskService deskService;
    private final EmployeeService employeeService;

    public DeskController(OfficeService officeService, DeskService deskService, EmployeeService employeeService) {
        this.officeService = officeService;
        this.deskService = deskService;
        this.employeeService = employeeService;
    }

    /**
     * GET Method that returns a view about the desks in our company.
     * Represents all desks, their offices and their assigned employee (optional).
     * If a desk has not been assigned to an employee, it will be represented as "Not assigned".
     */
    @GetMapping
    public String getDesksPage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String errorMessage,
                                 Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", errorMessage);
        }

        model.addAttribute("offices", officeService.findAll() != null ? officeService.findAll() : new ArrayList<Office>());
        model.addAttribute("desks", deskService.findAll() != null ? deskService.findAll() : new ArrayList<Desk>());
        model.addAttribute("employees", employeeService.findAll() != null ? employeeService.findAll() : new ArrayList<EmployeeService>());

        model.addAttribute("bodyContent", "desks");
        return "master-template";
    }

    /**
     * POST Method that saves the desk to the selected office.
     * Optionally, if an employee is passed as a parameter, it assigns the desk to the given employee.
     */
    @PostMapping("/addDesk")
    public String createDesk(@RequestParam int ordinal_number,
                             @RequestParam int office_id,
                             @RequestParam (required = false) String employee_id) {

        try {
            DeskDto deskDto = new DeskDto(ordinal_number, office_id);

            Desk desk = deskService.save(deskDto);
            if(employee_id != null && !employee_id.isEmpty()){
                employeeService.assignDesk(employee_id, desk.getDeskId());
            }

            return "redirect:/desks";
        } catch (Exception e) {
            return "redirect:/desks?error=true&errorMessage=" + e.getMessage();
        }
    }

    /**
     * DELETE Method that deletes the given desk and refreshes the page.
     */
    @GetMapping("/delete-desk/{desk_id}")
    public String deleteDesk(@PathVariable int desk_id){
        try{
            deskService.deleteById(desk_id);
            return "redirect:/desks";
        } catch(Exception e){
            return "redirect:/desks?error=true&errorMessage=" + e.getMessage();
        }
    }
}