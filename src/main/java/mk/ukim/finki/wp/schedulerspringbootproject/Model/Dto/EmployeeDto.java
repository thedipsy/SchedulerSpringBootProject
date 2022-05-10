package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;

@Data
public class EmployeeDto {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Role role;
    private int deskId;

    public EmployeeDto(String email, String name, String surname, String phone, Role role) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
    }

    public EmployeeDto(String email, String password, String name, String surname, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
    }


    public EmployeeDto(String email, String password, String name, String surname, String phone, Role role, int deskId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
        this.deskId = deskId;
    }
}
