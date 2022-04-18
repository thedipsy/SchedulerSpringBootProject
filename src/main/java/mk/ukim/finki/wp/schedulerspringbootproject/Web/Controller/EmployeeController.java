package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final OfficeService officeService;
    private final DeskService deskService;

    public EmployeeController(EmployeeService employeeService, OfficeService officeService, DeskService deskService) {
        this.employeeService = employeeService;
        this.officeService = officeService;
        this.deskService = deskService;
    }

    /**
     * GET Method that represents a view with listed employees.
     * Also, contains a form for registering new employees.
     */
    @GetMapping
    public String getEmployeesPage(@RequestParam(required = false) String error,
                                   @RequestParam(required = false) String errorMessage,
                                  Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", errorMessage);
        }

        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("desks", deskService.findAll());

        model.addAttribute("bodyContent", "employees");
        return "master-template";
    }

    /**
     * POST Method for registering a new employee.
     * Optionally, if a desk is passed, it assigns that desk to the newly created employee.
     */
    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String email,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String phone,
                              @RequestParam Role role,
                              @RequestParam int desk_id){
        try{
            EmployeeDto employeeDto = new EmployeeDto(email, name, surname, phone, role);
            Employee employee = employeeService.registerEmployee(employeeDto);
            if(desk_id != -1) {
                employeeService.assignDesk(employee.getEmail(), desk_id);
            }
            return "redirect:/employees";
        } catch(Exception e){
            return "redirect:/employees?error=true&errorMessage=" + e.getMessage();
        }
    }

    /**
     * DELETE Method that deletes the employee by its id (email)
     */
    @GetMapping("/delete-employee/{employee_id}")
    public String deleteEmployee(@PathVariable String employee_id){
        try{
            employeeService.deleteEmployee(employee_id);
            return "redirect:/employees";
        } catch(Exception e){
            return "redirect:/employees?error=true&errorMessage=" + e.getMessage();
        }
    }
}