package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final EmployeeService employeeService;

    public ProfileController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET Method that returns a view with user's information
     * Also provides a form for changing the password.
     */
    @GetMapping
    public String getProfilePage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String errorMessage,
                                 HttpServletRequest request,
                                 Model model) {
        if (error != null) {
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        if (request.getRemoteUser() != null) {
            Employee employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
            model.addAttribute(Constants.USER, employee);
        }

        model.addAttribute(Constants.BODY_CONTENT, "profile");
        return "master-template";
    }

    /**
     * POST Method for changing user's password.
     */
    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request,
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
            return "redirect:/profile?error=true&errorMessage=" + e.getMessage();
        }
    }

}

