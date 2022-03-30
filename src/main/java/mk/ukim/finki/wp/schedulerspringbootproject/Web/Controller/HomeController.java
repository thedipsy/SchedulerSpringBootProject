package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.Role;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.BookingNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.BookingService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
@RequestMapping(value={"/","/home"})
public class HomeController {

    private final EmployeeService employeeService;
    private final BookingService bookingService;

    public HomeController(EmployeeService employeeService, BookingService bookingService) {
        this.employeeService = employeeService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String getHomePage(@RequestParam(required = false) String error,
                              HttpServletRequest request,
                              Model model) {
        if (error != null) {
            model.addAttribute("hasError", true);
        }

        if (request.getRemoteUser() != null) {
            Employee employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
            model.addAttribute("user", employee);

            switch (employee.getRole()) {
                case ROLE_USER:
                    model.addAttribute("bookings", employee.getBookingList());
                    break;
                case ROLE_ADMIN:
                    model.addAttribute("bookings", bookingService.findAll());
            }
        }

        model.addAttribute("bodyContent", "reservation");
        return "master-template";
    }

    @PostMapping("/book")
    public String makeBooking(HttpServletRequest request,
                                  @RequestParam String date) {

        try {
            Employee employee;
            if (request.getRemoteUser() != null) {
                employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
                LocalDate bookingDate = LocalDate.parse(date);
                employeeService.makeBooking(employee, bookingDate);
            }

            return "redirect:/home#myReservations";
        } catch (Exception e) {
            return "redirect:/home?error=true#reserve";
        }
    }

    @GetMapping("/cancel-booking/{booking_id}")
    public String cancelReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.CANCELED);
            return "redirect:/home#myReservations";
        }catch (BookingNotFoundException exception) {
            return "redirect:/home#myReservations?error=true";
        }
    }

    @GetMapping("/accept-booking/{booking_id}")
    public String acceptReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.ACCEPTED);
            return "redirect:/home#requests";
        }catch (BookingNotFoundException exception) {
            return "redirect:/home#myReservations?error=true";
        }
    }

    @GetMapping("/reject-booking/{booking_id}")
    public String rejectReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.REJECTED);
            return "redirect:/home#requests";
        }catch (BookingNotFoundException exception) {
            return "redirect:/home#myReservations?error=true";
        }
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage (Model model){
        model.addAttribute("bodyContent", "access-denied");
        return "master-template";
    }

}
