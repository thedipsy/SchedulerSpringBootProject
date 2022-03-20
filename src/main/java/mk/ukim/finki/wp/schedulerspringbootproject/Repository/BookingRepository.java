package mk.ukim.finki.wp.schedulerspringbootproject.Repository;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


}
