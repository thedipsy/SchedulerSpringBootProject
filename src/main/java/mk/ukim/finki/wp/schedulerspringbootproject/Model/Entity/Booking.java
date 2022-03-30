package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    private LocalDate bookedDate;

    @ManyToOne
    private Employee employee;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    private Scheduler scheduler;

    public Booking() {
    }

    public Booking(LocalDate bookedDate, Employee employee) {
        this.bookedDate = bookedDate;
        this.employee = employee;
        this.status = BookingStatus.PENDING;
    }

    @Override
    public String toString() {
        return "bookingId=" + bookingId;
    }
}
