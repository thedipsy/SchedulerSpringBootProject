package mk.ukim.finki.wp.schedulerspringbootproject.Repository;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Integer> {
}
