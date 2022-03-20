package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;

@Data
public class EmployeeDto {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;

    public EmployeeDto(String email, String password, String name, String surname, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

}
