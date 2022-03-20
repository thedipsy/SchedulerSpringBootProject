package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;

public interface AuthService {

    Employee login(String username, String password);

}
