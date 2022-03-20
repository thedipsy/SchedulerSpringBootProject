package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.SchedulerRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.SchedulerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;

    }

    @Override
    public List<Booking> findAllBookings() {
        return null;//schedulerRepository.findAllBookings();
    }
}
