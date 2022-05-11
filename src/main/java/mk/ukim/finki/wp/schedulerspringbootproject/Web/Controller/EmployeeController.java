package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.*;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DeskService deskService;

    public EmployeeController(EmployeeService employeeService, DeskService deskService) {
        this.employeeService = employeeService;
        this.deskService = deskService;
    }

    /**
     * GET Method that represents a view with listed employees.
     * Also, contains a form for registering new employees.
     */
    @GetMapping
    public String getEmployeesPage(@RequestParam(required = false) String error,
                                   @RequestParam(required = false) String errorMessage,
                                  HttpServletRequest request,
                                  Model model) {
        if (error != null) {
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        List<Employee> allEmployees = employeeService.findAll();

        if (request.getRemoteUser() != null) {
            Employee employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
            allEmployees.remove(employee); //remove current user from the overview of employees
        }

        model.addAttribute(Constants.EMPLOYEES, allEmployees);

        List<Desk> desksWithoutEmployees = deskService.findAll()
                .stream()
                .filter(d -> d.getEmployee() == null)
                .collect(Collectors.toList());
        model.addAttribute(Constants.DESKS, desksWithoutEmployees);

        model.addAttribute(Constants.BODY_CONTENT, "employees");
        return "master-template";
    }

    /**
     * GET Method for representing information about selected employee to edit.
     */
    @GetMapping("/edit-employee/{employee_id}")
    public String getEditForm(@RequestParam(required = false) String error,
                                   @RequestParam(required = false) String errorMessage,
                                   @PathVariable String employee_id,
                              RedirectAttributes model) {
        if (error != null) {
            model.addFlashAttribute(Constants.HAS_ERROR, true);
            model.addFlashAttribute(Constants.ERROR, errorMessage);
        }

        model.addFlashAttribute(Constants.EMPLOYEES, employeeService.findAll());
        model.addFlashAttribute(Constants.SELECTED_EMPLOYEE, employeeService.findEmployeeByEmail(employee_id));

        List<Desk> desksWithoutEmployees = deskService.findAll()
                .stream()
                .filter(d -> d.getEmployee() == null)
                .collect(Collectors.toList());
        model.addFlashAttribute(Constants.DESKS, desksWithoutEmployees);

        return "redirect:/employees#employeeForm";
    }

    /**
     * POST Method for registering a new employee or applying changes to existing ones.
     * Optionally, if a desk is passed, it assigns that desk to the newly created employee.
     */
    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam(required = false) String selectedEmployeeId,
                              @RequestParam String email,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String phone,
                              @RequestParam Role role,
                              @RequestParam int desk_id){
        try{
            Employee employee;
            if(selectedEmployeeId != null && !selectedEmployeeId.isEmpty()){
                employee = employeeService.findEmployeeByEmail(selectedEmployeeId);
                employeeService.edit(selectedEmployeeId, email, name, surname, phone, role);
            }
            else{
                EmployeeDto employeeDto = new EmployeeDto(email, name, surname, phone, role);
                employee = employeeService.registerEmployee(employeeDto);
            }
            if(desk_id != -1) {

                if(selectedEmployeeId == null || employee.getDesk() == null || employee.getDesk().getDeskId() != desk_id) {
                    employeeService.assignDesk(employee.getEmail(), desk_id);
                }
            } else {
                if(selectedEmployeeId != null && employee.getDesk() != null){
                    employeeService.deleteDeskFromEmployee(employee.getEmail());
                }
            }
            return "redirect:/employees";
        } catch(IllegalArgumentException | EmployeeNotFound | EmailAlreadyUsedException | UsernameNotFoundException |
                DeskNotFoundException | DeskAlreadyAssignedException | InvalidUsernameOrPasswordException e){
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
            return "redirect:/employees?error=true";
        }
    }
}