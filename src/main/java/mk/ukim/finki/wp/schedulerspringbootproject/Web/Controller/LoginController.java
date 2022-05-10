package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final EmployeeService employeeService;

    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET Method that returns a view for a login form
     */
    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String errorMessage,
                               HttpServletRequest request,
                               Model model) {

        if(error != null){
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

//        create first employee manually
//        EmployeeDto employeeDto = new EmployeeDto(
//                "wp.workscheduler@gmail.com",
//                "Admin",
//                "Admin",
//                "38976999298",
//                Role.ROLE_ADMIN
//        );
//        employeeService.registerEmployee(employeeDto);

        if(request.getRemoteUser() != null){
            return "redirect:/home";
        }

        model.addAttribute(Constants.BODY_CONTENT, "login");
        return "master-template";
    }


}
