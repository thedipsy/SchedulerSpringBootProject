package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Desk;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.DeskNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.OfficeNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.DeskDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.DeskRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.OfficeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.DeskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Optional;

@Service
public class DeskServiceImpl implements DeskService {

    private final DeskRepository deskRepository;
    private final OfficeRepository officeRepository;
    private final EmployeeRepository employeeRepository;

    public DeskServiceImpl(DeskRepository deskRepository, OfficeRepository officeRepository, EmployeeRepository employeeRepository) {
        this.deskRepository = deskRepository;
        this.officeRepository = officeRepository;
        this.employeeRepository = employeeRepository;
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

    @Override
    public Optional<Desk> edit(int id, int ordinal_number, int office_id) {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);

        Office office = officeRepository.findById(office_id)
                .orElseThrow(OfficeNotFoundException::new);

        desk.setOffice(office);
        desk.setOrdinalNumber(ordinal_number);

        return Optional.of(deskRepository.save(desk));

    }

    /**
     * Deletes a desk by its id
     */

    @Transactional
    @Override
    public void deleteById(int id) {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(DeskNotFoundException::new);

        if(desk.getEmployee() != null) {
            Employee employee = desk.getEmployee();
            employee.setDesk(null);
            employeeRepository.save(employee);
        }

        deskRepository.deleteById(id);
    }

}
