package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.BookingDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> findAll();
    Booking findById(int id);
    Booking save(BookingDto bookingDto);
    void deleteById(int id);
    Optional<Booking> update(int booking_id, BookingStatus canceled);
}
