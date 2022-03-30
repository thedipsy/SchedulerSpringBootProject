package mk.ukim.finki.wp.schedulerspringbootproject.Jobs;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    private final BookingService bookingService;

    public ScheduledTasks(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(cron = "0 00 22 * * ?") //updateStatus pending expired bookings every day at 10pm
    public void updateExpiredBookings() {
        List<Booking> bookings = bookingService.findAll();
        bookings.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.PENDING))
                .forEach(b ->{
                    if(hasDatePassed(b)){
                                bookingService.updateStatus(b.getBookingId(), BookingStatus.EXPIRED);
                        }
                });
    }

    @Scheduled(cron = "0 0 08 * * ?") //updateStatus accepted bookings on current day every day at 8am
    public void updateInProgressBookings(){
        List<Booking> bookings = bookingService.findAll();
        bookings.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.ACCEPTED))
                .forEach(b ->{
                    if(hasDatePassed(b)){
                        bookingService.updateStatus(b.getBookingId(), BookingStatus.IN_PROGRESS);
                        //TOCHANGE isCurrentDay instead of has DatePassed
                    }
                });
    }

    @Scheduled(cron = "0 0 16 * * ?") //updateStatus finished bookings every day at 4pm
    public void updateFinishedBookings(){
        List<Booking> bookings = bookingService.findAll();
        bookings.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.IN_PROGRESS))
                .forEach(b ->{
                    if(hasDatePassed(b)){
                        bookingService.updateStatus(b.getBookingId(), BookingStatus.FINISHED);
                    }
                });
    }

    private boolean hasDatePassed(Booking b) {
        return LocalDate.now().isAfter(b.getBookedDate());
    }

}

