package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Scheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schedulerId;

    @OneToMany(mappedBy = "scheduler")
    private List<Booking> bookingList;

    public Scheduler() {}

//    public Scheduler(List<Booking> bookingList) {
//        this.bookingList = bookingList;
//    }

}
