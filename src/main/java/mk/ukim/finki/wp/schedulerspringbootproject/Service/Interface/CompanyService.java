package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;


import mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto.CompanyDto;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();
    Company save(CompanyDto companyDto);
    void deleteById(int companyId);

}
