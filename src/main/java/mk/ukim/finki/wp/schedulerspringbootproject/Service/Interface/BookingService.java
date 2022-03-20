package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.BookingDto;

import java.util.List;

public interface BookingService {

    List<Booking> findAll();
    Booking findById(int id);
    Booking save(BookingDto bookingDto);
    void deleteById(int id);

}
