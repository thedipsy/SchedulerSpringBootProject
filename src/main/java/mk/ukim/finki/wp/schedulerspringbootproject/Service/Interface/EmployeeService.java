package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.PasswordsDoNotMatchException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService extends UserDetailsService {

    List<Employee> findAll();
    Employee registerEmployee(EmployeeDto employeeDto) throws PasswordsDoNotMatchException, InvalidUsernameOrPasswordException;
    Employee findEmployeeByEmail(String email);
    void makeBooking(Employee employee, LocalDate date);
    void changePassword(Employee employee, String old_password, String password, String confirm_password);
    Employee assignDesk(String employee, int desk_id);
    void deleteEmployee(String employee_id);
}
