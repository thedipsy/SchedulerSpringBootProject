package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final EmployeeService employeeService;

    public ProfileController(EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getProfilePage(@RequestParam(required = false) String error,
                                 HttpServletRequest request,
                                 Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
        }

        if (request.getRemoteUser() != null) {
            Employee employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
            model.addAttribute("user", employee);
        }

        model.addAttribute("bodyContent", "profile");

        return "master-template";
    }

    @PostMapping("/change-password")
    public String addCar(HttpServletRequest request,
                         @RequestParam String old_password,
                         @RequestParam String new_password,
                         @RequestParam String confirm_password) {

        try {
            Employee employee;
            if (request.getRemoteUser() != null) {
                employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
                employeeService.changePassword(employee, old_password, new_password, confirm_password);
            }
            return "redirect:/profile";
        } catch (Exception e) {
            return "redirect:/profile?error=true";
        }
    }

}

