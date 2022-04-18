package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.BookingNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.UserNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.BookingDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.BookingRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.BookingService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository, EmployeeRepository employeeRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    /**
     * Returns all bookings
     */
    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * Returns a booking by id
     */
    @Override
    public Booking findById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);
    }

    /**
     * Saves a booking in the database with specified employee
     * Throws an exception if the employee is not found
     */
    @Override
    public Booking save(BookingDto bookingDto) {
        Employee employee = employeeRepository.findById(bookingDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(bookingDto.getEmail()));

        Booking booking = new Booking(bookingDto.getBookedDate(), employee);
        return bookingRepository.save(booking);
    }

    /**
     * Updates the status of the booking, and informs the employee by sending a mail via email service
     */
    @Override
    public Optional<Booking> updateStatus(int bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
        booking.setStatus(status);
        bookingRepository.save(booking);

        //send email for update of status
        String to = booking.getEmployee().getEmail();
        String topic = "Booking Updated!";
        String body = "Your booking has been updated.\nStatus: " + status;

        emailService.sendEmail(to, topic, body);

        return Optional.of(booking);
    }

    /**
     * Deletes a booking by its id
     */
    @Override
    public void deleteById(int id) {
         bookingRepository.deleteById(id);
    }
}
