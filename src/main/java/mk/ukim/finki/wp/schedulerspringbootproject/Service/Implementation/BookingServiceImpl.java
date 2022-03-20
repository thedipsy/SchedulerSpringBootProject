package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.BookingNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.UserNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.BookingDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.BookingRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return bookingRepository.findAll();
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
    public void deleteById(int id) {
         bookingRepository.deleteById(id);
    }
}
