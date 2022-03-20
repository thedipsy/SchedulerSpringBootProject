package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;

import java.util.List;

public interface DeskService {

    List<Desk> findAll();
    Desk findById(int id);
    Desk save(DeskDto deskDto);
    void deleteById(int id);

}
