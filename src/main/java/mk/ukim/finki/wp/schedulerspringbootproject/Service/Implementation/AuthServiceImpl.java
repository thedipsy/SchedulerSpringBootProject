package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidCredentialsException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;

    public AuthServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee login(String email, String password) {
        if(email == null || email.isEmpty() || password == null || password.isEmpty()){
            throw new InvalidUsernameOrPasswordException();
        }

        return employeeRepository.findByEmailAndPassword(email, password)
                .orElseThrow(InvalidCredentialsException::new);
    }

}
