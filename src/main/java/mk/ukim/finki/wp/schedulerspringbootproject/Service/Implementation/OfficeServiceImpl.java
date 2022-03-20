package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.OfficeNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.OfficeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.OfficeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeServiceImpl(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    @Override
    public Office findById(int id) {
        return officeRepository.findById(id)
                .orElseThrow(OfficeNotFoundException::new);
    }

    @Override
    public Office save(OfficeDto officeDto) {
        Office office = new Office(officeDto.getOrdinalNumber());

        return officeRepository.save(office);
    }

    @Override
    public void deleteById(int id) {
        officeRepository.deleteById(id);
    }
}
