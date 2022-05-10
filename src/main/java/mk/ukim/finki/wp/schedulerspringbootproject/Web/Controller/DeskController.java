package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        model.addAttribute(Constants.OFFICES, officeService.findAll());
        model.addAttribute(Constants.DESKS, deskService.findAll());

        List<Employee> employeesWithoutDesks = employeeService.findAll()
                        .stream()
                        .filter(e -> e.getDesk() == null)
                        .collect(Collectors.toList());
        model.addAttribute(Constants.EMPLOYEES, employeesWithoutDesks);

        model.addAttribute(Constants.BODY_CONTENT, "desks");
        return "master-template";
    }

    /**
     * POST Method that saves the desk to the selected office.
     * Optionally, if an employee is passed as a parameter, it assigns the desk to the given employee.
     */
    @PostMapping("/addDesk")
    public String createDesk(@RequestParam(required = false) Long id,
                             @RequestParam int ordinal_number,
                             @RequestParam int office_id,
                             @RequestParam (required = false) String employee_id) {

        try {
            Desk desk;
            if(id != null) {
                desk = deskService.findById(id.intValue());
                deskService.edit(id.intValue(), ordinal_number, office_id);
            }
            else {
                DeskDto deskDto = new DeskDto(ordinal_number, office_id);
                desk = deskService.save(deskDto);
            }

            if(employee_id != null && !employee_id.isEmpty()){
                if(id == null || desk.getEmployee() == null || !(desk.getEmployee().getEmail().equals(employee_id))) {
                    employeeService.assignDesk(employee_id, desk.getDeskId());
                }
            }

            return "redirect:/desks";
        } catch (Exception e) {
            return "redirect:/desks?error=true&errorMessage=" + e.getMessage();
        }
    }

    /**
     * GET Method that used for representing data before changing it.
     */
    @GetMapping("/edit-desk/{desk_id}")
    public String getEditForm(@PathVariable int desk_id,
                             Model model){
        try{
            Desk desk = deskService.findById(desk_id);
            model.addAttribute(Constants.SELECTED_DESK, desk);
            model.addAttribute(Constants.OFFICES, officeService.findAll());
            model.addAttribute(Constants.DESKS, deskService.findAll());
            model.addAttribute(Constants.EMPLOYEES, employeeService.findAll());

            model.addAttribute(Constants.BODY_CONTENT, "desks");
            return "master-template";
        } catch(Exception e){
            return "redirect:/desks?error=true";
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
            return "redirect:/desks?error=true";
        }
    }
}