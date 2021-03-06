package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.CompanyDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Company;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.CompanyNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.EmailAlreadyUsedException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidCompanyKeyException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.CompanyService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(EmployeeService employeeService, CompanyService companyService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.companyService = companyService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * GET Method that represents a view with listed employees.
     * Also, contains a form for registering new employees.
     */
    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error,
                                   @RequestParam(required = false) String errorMessage,
                                   Model model) {
//        CompanyDto company1 = new CompanyDto("Company 1", "9ti maj 36/1", "+38976999298", passwordEncoder.encode("secret"));
//        CompanyDto company2 = new CompanyDto("Company 2", "9ti maj 36/1", "+38976999298", passwordEncoder.encode("secret"));
//        companyService.save(company1);
//        companyService.save(company2);

        if (error != null) {
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        model.addAttribute(Constants.COMPANIES, companyService.findAll());

        model.addAttribute(Constants.BODY_CONTENT, "register");
        return "master-template";
    }

    /**
     * POST Method for registering a new employee or applying changes to existing ones.
     * Optionally, if a desk is passed, it assigns that desk to the newly created employee.
     */
    @PostMapping
    public String register(   @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String phone,
                              @RequestParam int company_id,
                              @RequestParam String company_key){
        try{

                EmployeeDto employeeDto = new EmployeeDto(email, password, name, surname, phone, Role.ROLE_ADMIN);
                employeeService.registerAdmin(employeeDto, company_id, company_key);

            return "redirect:/login";
        } catch(InvalidUsernameOrPasswordException | CompanyNotFoundException |
                InvalidCompanyKeyException | EmailAlreadyUsedException e){
            return "redirect:/register?error=true&errorMessage=" + e.getMessage();
        }
    }
}
