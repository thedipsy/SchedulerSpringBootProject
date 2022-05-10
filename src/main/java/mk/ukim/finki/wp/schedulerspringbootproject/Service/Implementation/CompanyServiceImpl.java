package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.CompanyDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Company;
import mk.ukim.finki.wp.schedulerspringbootproject.Repository.CompanyRepository;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company save(CompanyDto companyDto) {
        Company company = new Company(
                companyDto.getName(),
                companyDto.getAddress(),
                companyDto.getFaxNumber(),
                companyDto.getCompanyKey()
        );
        return companyRepository.save(company);
    }

    @Override
    public void deleteById(int companyId) {
        companyRepository.deleteById(companyId);
    }

}
