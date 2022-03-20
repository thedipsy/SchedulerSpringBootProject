package mk.ukim.finki.wp.schedulerspringbootproject.Repository;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Integer> {
}
