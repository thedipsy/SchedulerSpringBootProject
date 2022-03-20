package mk.ukim.finki.wp.schedulerspringbootproject.Repository;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmailAndPassword(String email, String password);
}
