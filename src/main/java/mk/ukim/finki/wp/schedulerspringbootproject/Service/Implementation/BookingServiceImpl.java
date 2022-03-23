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
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, EmployeeRepository employeeRepository) {
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        bookings = checkPassedDates(bookings);
        return bookings;
    }

    private List<Booking> checkPassedDates(List<Booking> bookings) {
        return bookings.stream()
                .peek(b -> {
                        if(LocalDate.now().isAfter(b.getBookedDate())){
                            switch (b.getStatus()){
                                case ACCEPTED:
                                    b.setStatus(BookingStatus.FINISHED);
                                    break;
                                case PENDING:
                                    b.setStatus(BookingStatus.EXPIRED);
                                    break;
                            }
                        }
                }).collect(Collectors.toList());
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Booking save(BookingDto bookingDto) {
        Employee employee = employeeRepository.findById(bookingDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(bookingDto.getEmail()));

        Booking booking = new Booking(bookingDto.getBookedDate(), employee);

        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> update(int bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);
        booking.setStatus(status);
        bookingRepository.save(booking);
        return Optional.of(booking);
    }

    @Override
    public void deleteById(int id) {
         bookingRepository.deleteById(id);
    }
}
