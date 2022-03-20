package mk.ukim.finki.wp.schedulerspringbootproject.Repository;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<Scheduler, Integer> {
}
