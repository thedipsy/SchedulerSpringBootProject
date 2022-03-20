package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;

import java.util.List;

public interface SchedulerService {

    List<Booking> findAllBookings();


}
