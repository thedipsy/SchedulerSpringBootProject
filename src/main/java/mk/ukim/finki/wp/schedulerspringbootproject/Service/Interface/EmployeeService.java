package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.PasswordsDoNotMatchException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeService extends UserDetailsService {

    List<Employee> findAll();
    Employee registerEmployee(EmployeeDto employeeDto) throws PasswordsDoNotMatchException, InvalidUsernameOrPasswordException;
    Optional<Employee> edit(String employee_id, String email, String name, String surname, String phone, Role role);
    Employee findEmployeeByEmail(String email);
    void makeBooking(Employee employee, LocalDate date);
    void changePassword(Employee employee, String old_password, String password, String confirm_password);
    Employee assignDesk(String employee_id, int desk_id);
    Employee deleteDeskFromEmployee(String employee_id);
    void deleteEmployee(String employee_id);
    Employee registerAdmin(EmployeeDto employeeDto, int company_id, String company_key);


}
