package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final EmployeeService employeeService;

    public RegisterController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error,
                                  Model model){
        if(error != null){
            model.addAttribute("hasError", true);
        }
        model.addAttribute("bodyContent", "register");

        return "master-template";
    }

    @PostMapping
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String phone){
        try{
            EmployeeDto employeeDto = new EmployeeDto(email, password, name, surname, phone);
            employeeService.register(employeeDto, confirmPassword);
            return "redirect:/login";
        } catch(Exception e){
            return "redirect:/register?error=true";
        }
    }

}

