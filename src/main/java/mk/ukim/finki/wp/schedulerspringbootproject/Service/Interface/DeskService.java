package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;

import java.util.List;
import java.util.Optional;

public interface DeskService {

    List<Desk> findAll();
    Desk findById(int id);
    Optional<Desk> findByOrdinalNumber(int ordinalNumber);
    Desk save(DeskDto deskDto);
    Optional<Desk> edit(int id, int ordinal_number, int office_id);
    void deleteById(int id);
    void deleteEmployeeFromDesk(Desk desk);
}
