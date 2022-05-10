package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyId;

    private String name;

    private String address;

    private String faxNumber;

    private String companyKey;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Employee> employeeList;

    public Company() {}

    public Company(String name, String address, String faxNumber, String companyKey) {
        this.name = name;
        this.address = address;
        this.faxNumber = faxNumber;
        this.companyKey = companyKey;
    }

}
