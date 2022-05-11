package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Company;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.*;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.EmployeeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.BookingRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.CompanyRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.DeskRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmailService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final CompanyRepository companyRepository;
    private final DeskRepository deskRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BookingRepository bookingRepository, CompanyRepository companyRepository, DeskRepository deskRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.companyRepository = companyRepository;
        this.deskRepository = deskRepository;
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

    @Override
    public Employee registerAdmin(EmployeeDto employeeDto, int company_id, String company_key) {
        if(employeeDto.getEmail() == null || employeeDto.getEmail().isEmpty()){
            throw new InvalidUsernameOrPasswordException();
        }

        Company company = companyRepository.findById(company_id)
                .orElseThrow(CompanyNotFoundException::new);

        if(!passwordEncoder.matches(company_key, company.getCompanyKey())){
            throw new InvalidCompanyKeyException();
        }

        if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()){
            throw new EmailAlreadyUsedException(employeeDto.getEmail());
        }

        //encode password
        String encodedPassword = passwordEncoder.encode(employeeDto.getPassword());

        Employee employee = new Employee(
                employeeDto.getEmail(),
                encodedPassword,
                employeeDto.getName(),
                employeeDto.getSurname(),
                employeeDto.getPhone(),
                employeeDto.getRole()
        );

        employee.setCompany(company);
        employeeRepository.save(employee);

        //Send password via email only if the registering had succeeded
        String body = Constants.EMAIL_BODY_WELCOME_ADMIN;
        String title = Constants.EMAIL_TITLE_WELCOME;
        emailService.sendEmail(employeeDto.getEmail(), title, body);
        return employee;
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
        String body = Constants.EMAIL_BODY_WELCOME_EMPLOYEE + generatedPassword + "\n" + Constants.EMAIL_BODY_CHANGE_PASSWORD;
        String title = Constants.EMAIL_TITLE_WELCOME_EMPLOYEE_PASSWORD;
        emailService.sendEmail(employeeDto.getEmail(), title, body);
        return employee;
    }

    /**
     * Returns a 10 characters random generated password containing 0-9, a-z, A-Z
     */
    public static String generatePassword() {
        String alphaNumeric = Constants.ALPHA_NUMERIC;
        Random random = new Random();

        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(alphaNumeric.charAt(random.nextInt(alphaNumeric.length())));
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
     * Saves a request for the selected employee on the given date
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

    @Override
    public Optional<Employee> edit(String id, String email, String name, String surname, String phone, Role role) {
        if(email == null || email.isEmpty() || name == null || name.isEmpty() ||
                surname == null || surname.isEmpty() || phone == null || phone.isEmpty()){
            //null or empty fields
            throw new IllegalArgumentException();
        }
        Employee employee = employeeRepository.findByEmail(id)
                .orElseThrow(EmployeeNotFound::new);

        employee.setName(name);
        employee.setSurname(surname);
        employee.setPhone(phone);
        employee.setRole(role);

        if(!employee.getEmail().equals(email)) {
            if (employeeRepository.findByEmail(email).isEmpty()) {
                employee.setEmail(email);
            } else {
                throw new EmailAlreadyUsedException(email);
            }
        }

        return Optional.of(employeeRepository.save(employee));
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
        Desk desk = deskRepository.findById(desk_id)
                .orElseThrow(DeskNotFoundException::new);
        if(desk.getEmployee() != null){
            throw new DeskAlreadyAssignedException(desk.getOrdinalNumber());
        }
        employee.setDesk(desk);
        return employeeRepository.save(employee);
    }


    @Override
    public Employee deleteDeskFromEmployee(String employee_id) {
        Employee employee = employeeRepository.findByEmail(employee_id)
                .orElseThrow(() -> new UsernameNotFoundException(employee_id));
        employee.setDesk(null);

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String employee_id) {
        employeeRepository.deleteById(employee_id);
    }

}
