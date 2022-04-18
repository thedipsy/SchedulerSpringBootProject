package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.DeskNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.OfficeNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.DeskRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.OfficeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskServiceImpl implements DeskService {

    private final DeskRepository deskRepository;
    private final OfficeRepository officeRepository;

    public DeskServiceImpl(DeskRepository deskRepository, OfficeRepository officeRepository) {
        this.deskRepository = deskRepository;
        this.officeRepository = officeRepository;
    }

    /**
     * Returns all desks
     */
    @Override
    public List<Desk> findAll() {
        return deskRepository.findAll();
    }

    /**
     * Returns a desk by its id
     */
    @Override
    public Desk findById(int id) {
        return deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);
    }

    /**
     * Saves a desk in a specified office
     * Throws an exception if the office is not found
     */
    @Override
    public Desk save(DeskDto deskDto) {
       Office office = officeRepository.findById(deskDto.getOfficeId())
               .orElseThrow(OfficeNotFoundException::new);

       Desk desk = new Desk(deskDto.getOrdinalNumber(), office);
       return deskRepository.save(desk);
    }

    /**
     * Deletes a desk by its id
     */
    @Override
    public void deleteById(int id) {
        deskRepository.deleteById(id);
    }

}
