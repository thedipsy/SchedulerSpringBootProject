package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    private Date bookedDate;

    @ManyToOne
    private Employee employee;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    private Scheduler scheduler;

    public Booking() {
    }

    public Booking(Date bookedDate, Employee employee) {
        this.bookedDate = bookedDate;
        this.employee = employee;
        this.status = BookingStatus.CREATED;
    }

}
