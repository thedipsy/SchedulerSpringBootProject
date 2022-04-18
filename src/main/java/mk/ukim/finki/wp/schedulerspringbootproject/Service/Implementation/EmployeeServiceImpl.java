package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.*;
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

    /**
     * Returns all employees
     */
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Admin can register an employee with a random generated password.
     * Employee receives the password via the mail that is registered to.
     * Throws an exception if email is invalid or if the email is already registered.
     */
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

    /**
     * Returns a 10 characters random generated password containing 0-9, a-z, A-Z
     * @return
     */
    public static String generatePassword() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    /**
     * Returns UserDetails object of an employee registered with email sent as a parameter
     * Used for authentication
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * Returns an employee with specified email
     */
    @Override
    public Employee findEmployeeByEmail(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * Saves a booking for the selected employee on the given date
     */
    @Override
    public void makeBooking(Employee employee, LocalDate date) {
        Booking booking = new Booking(date, employee);
        bookingRepository.save(booking);
    }

    /**
     * Method used for changing employee's password, updating the employee
     * Throws an exception if the old password is not correct
     * Throws an exception if the new passwords do not match
     */
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

    /**
     * Method used for assigning specific desk to a specific user
     * Each user can have one desk, and each desk can belong to one user
     * Throws an exception if the desk is already assigned to another employee
     */
    @Override
    public Employee assignDesk(String employee_id, int desk_id) {
        Employee employee = employeeRepository.findByEmail(employee_id)
                        .orElseThrow(() -> new UsernameNotFoundException(employee_id));
        Desk desk = deskService.findById(desk_id);
        if(desk.getEmployee() != null){
            throw new DeskAlreadyAssignedException(desk.getOrdinalNumber());
        }
        employee.setDesk(desk);
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String employee_id) {
        employeeRepository.deleteById(employee_id);
    }

}
