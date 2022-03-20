package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.OfficeDto;

import java.util.List;

public interface OfficeService {

    List<Office> findAll();
    Office findById(int id);
    Office save(OfficeDto officeDto);
    void deleteById(int id);

}
