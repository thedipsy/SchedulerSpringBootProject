package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.EmailAlreadyUsedException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.IncorrectPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.InvalidUsernameOrPasswordException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.PasswordsDoNotMatchException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.BookingRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmailService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final DeskService deskService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BookingRepository bookingRepository, DeskService deskService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.deskService = deskService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee registerEmployee(EmployeeDto employeeDto) throws PasswordsDoNotMatchException, InvalidUsernameOrPasswordException {
        if(employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty()){
            throw new InvalidUsernameOrPasswordException();
        }

        if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()){
            throw new EmailAlreadyUsedException(employeeDto.getEmail());
        }

        //generate password
        String generatedPassword = generatePassword();
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        Employee employee = new Employee(
                employeeDto.getEmail(),
                encodedPassword,
                employeeDto.getName(),
                employeeDto.getSurname(),
                employeeDto.getPhone(),
                employeeDto.getRole()
        );
        employeeRepository.save(employee);

        //Send password via email only if the registering had succeeded
        String body = "Welcome!\nYou are now registered on Work Scheduler.\nTo login use the following generated password " + generatedPassword +
                "\nPlease change the password once you log in for security reasons.";
        emailService.sendEmail(employeeDto.getEmail(), body,"My Work Scheduler Password" );
        return employee;
    }

    public static String generatePassword() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
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

    @Override
    public void makeBooking(Employee employee, LocalDate date) {
        Booking booking = new Booking(date, employee);
        bookingRepository.save(booking);
    }

    @Override
    public void changePassword(Employee employee, String old_password, String new_password, String confirm_password) {
        if(old_password == null || old_password.isEmpty() ||
                new_password == null || new_password.isEmpty() ||
                confirm_password == null || confirm_password.isEmpty()){
            //null or empty fields
            throw new IllegalArgumentException();
        }

        if(!passwordEncoder.matches(old_password, employee.getPassword())){
            //incorrect old password
            throw new IncorrectPasswordException();
        }
        if(!new_password.equals(confirm_password)){
            //passwords do not match
            throw new PasswordsDoNotMatchException();
        }

        //change password
        String encodedPassword = passwordEncoder.encode(new_password);
        employee.setPassword(encodedPassword);
        employeeRepository.save(employee);
    }

    @Override
    public Employee assignDesk(String employee_id, int desk_id) {
        Desk desk = deskService.findById(desk_id);
        Employee employee = employeeRepository.findByEmail(employee_id)
                        .orElseThrow(() -> new UsernameNotFoundException(employee_id));
        employee.setDesk(desk);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String employee_id) {
        employeeRepository.deleteById(employee_id);
    }

}
