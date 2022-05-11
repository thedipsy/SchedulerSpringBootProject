package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.OfficeNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Office;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.OfficeDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.UniqueOrdinalNumberException;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.EmployeeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.OfficeRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.OfficeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final EmployeeRepository employeeRepository;

    public OfficeServiceImpl(OfficeRepository officeRepository, EmployeeRepository employeeRepository) {
        this.officeRepository = officeRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Returns all offices
     */
    @Override
    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    /**
     * Returns an office by its id
     */
    @Override
    public Office findById(int id) {
        return officeRepository.findById(id)
                .orElseThrow(OfficeNotFoundException::new);
    }

    @Override
    public Optional<Office> findByOrdinalNumber(int ordinalNumber) {
        return officeRepository.findByOrdinalNumber(ordinalNumber);
    }

    /**
     * Saves a new office to database
     */
    @Override
    public Office save(OfficeDto officeDto) {
        if(findByOrdinalNumber(officeDto.getOrdinalNumber()).isPresent()){
            throw new UniqueOrdinalNumberException();
        }
        Office office = new Office(officeDto.getOrdinalNumber());

        return officeRepository.save(office);
    }

    /**
     * Deletes an office by its id
     */
    @Transactional
    @Override
    public void deleteById(int id) {
        //get selected office
        Office office = officeRepository.findById(id)
                        .orElseThrow(OfficeNotFoundException::new);

        //list the desks in the office
        //for each desk that has an assigned employee, find the employee, delete its desk and save the employee
        office.getDeskList()
                .forEach(d -> {
                    if(d.getEmployee() != null){
                        Employee employee = d.getEmployee();
                        employee.setDesk(null);
                        employeeRepository.save(employee);
                    }
                });
        officeRepository.deleteById(id);
    }
}
