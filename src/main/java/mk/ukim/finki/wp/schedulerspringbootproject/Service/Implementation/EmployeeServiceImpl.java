package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.EmailAlreadyUsedException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.PasswordsDoNotMatchException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee register(EmployeeDto employeeDto, String repeatPassword) throws PasswordsDoNotMatchException, InvalidUsernameOrPasswordException {
        if(employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty() ||
                employeeDto.getPassword() == null || employeeDto.getPassword().isEmpty()){
            throw new InvalidUsernameOrPasswordException();
        }

        if(!employeeDto.getPassword().equals(repeatPassword)){
            throw new PasswordsDoNotMatchException();
        }

        if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()){
            throw new EmailAlreadyUsedException(employeeDto.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(employeeDto.getPassword());

        Employee employee = new Employee(
                employeeDto.getEmail(),
                encodedPassword,
                employeeDto.getName(),
                employeeDto.getSurname(),
                employeeDto.getPhone(),
                Role.ROLE_USER
        );

        return employeeRepository.save(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public Employee findEmployeeByEmail(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
