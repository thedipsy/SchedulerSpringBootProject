package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;

@Data
public class CompanyDto {

    private String name;
    private String address;
    private String faxNumber;
    private String companyKey;

    public CompanyDto(String name, String address, String faxNumber, String companyKey) {
        this.name = name;
        this.address = address;
        this.faxNumber = faxNumber;
        this.companyKey = companyKey;
    }

}
