package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.PasswordsDoNotMatchException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface EmployeeService extends UserDetailsService {

    Employee register(EmployeeDto employeeDto, String repeatPassword) throws PasswordsDoNotMatchException, InvalidUsernameOrPasswordException;
    Employee findEmployeeByEmail(String email);
}
