package mk.ukim.finki.wp.schedulerspringbootproject.Config;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;

import java.time.LocalDate;

public class HelperClass {

    /**
     * Method that returns true if the booked date from the booking has passed, returns false if the date is in the future
     */
    public static boolean hasDatePassed(Booking b) {
        return LocalDate.now().isAfter(b.getBookedDate());
    }

    /**
     * Method that returns true if the booked date is on the current date
     */
    public static boolean isToday(Booking b) {
        return LocalDate.now().equals(b.getBookedDate());
    }
}
