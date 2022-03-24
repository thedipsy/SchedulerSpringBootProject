package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class Employee implements UserDetails {

    @Id
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;

    @OneToOne
    private Desk desk;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    //@JsonIgnore
    private List<Booking> bookingList;

    public Employee() {
    }

    public Employee(String email, String password, String name, String surname, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
        this.bookingList = new ArrayList<>();
    }

    //----------------------- USER DETAILS SECTION ---------------------------------
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialNonExpired = true;
    private boolean isEnabled = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    //----------------------- END USER DETAILS SECTION -----------------------------


    @Override
    public String toString() {
        return String.format("%s %s", name, surname);
    }
}
