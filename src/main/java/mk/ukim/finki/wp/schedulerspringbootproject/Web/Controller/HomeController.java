package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Booking;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity.Employee;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Exception.BookingNotFoundException;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.BookingService;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value={"/","/home"})
public class HomeController {

    private final EmployeeService employeeService;
    private final BookingService bookingService;

    public HomeController(EmployeeService employeeService, BookingService bookingService) {
        this.employeeService = employeeService;
        this.bookingService = bookingService;
    }

    /**
     * GET Method that represents the home page.
     * Two perspectives:
     * -User: lists user's bookings, also a booking form is provided for creating a new booking.
     * -Admin: lists all bookings, all requests and gives option to accept or decline bookings.
     */
    @GetMapping
    public String getHomePage(@RequestParam(required = false) String error,
                              @RequestParam(required = false) String errorMessage,
                              HttpServletRequest request,
                              Model model) {
        if (error != null) {
            model.addAttribute(Constants.HAS_ERROR, true);
            model.addAttribute(Constants.ERROR, errorMessage);
        }

        if (request.getRemoteUser() != null) {
            Employee employee = employeeService.findEmployeeByEmail(request.getRemoteUser());
            model.addAttribute(Constants.USER, employee);

            switch (employee.getRole()) {
                case ROLE_USER:
                    model.addAttribute(Constants.BOOKINGS, employee.getBookingList());
                    model.addAttribute(Constants.CURRENT_DATE, LocalDate.now());
                    break;
                case ROLE_ADMIN:
                    List<Booking> bookings =  bookingService.findAdminOverviewBookings();
                    model.addAttribute(Constants.BOOKINGS, bookings);

                    List<Booking> requests = bookingService.findAdminOverviewRequests();
                    model.addAttribute(Constants.REQUESTS, requests);
                    break;
            }
        }

        model.addAttribute(Constants.BODY_CONTENT, "home");
        return "master-template";
    }

    /**
     * POST Mapping that allows users to make a new booking.
     *
     */
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
            return "redirect:/home#reserve?error=true&errorMessage=" + e.getMessage();
        }
    }

    /**
     * GET Mapping for admin canceling employee's booking.
     */
    @GetMapping("/cancel-booking/{booking_id}")
    public String cancelReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.CANCELED);
            return "redirect:/home#myReservations";
        }catch (BookingNotFoundException e) {
            return "redirect:/home#myReservations?error=true&errorMessage" + e.getMessage();
        }
    }

    /**
     * GET Mapping for admin accepting employee's booking.
     */
    @GetMapping("/accept-booking/{booking_id}")
    public String acceptReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.ACCEPTED);
            return "redirect:/home#overview";
        }catch (BookingNotFoundException e) {
            return "redirect:/home#myReservations?error=true&errorMessage=" + e.getMessage();
        }
    }

    /**
     * GET Mapping for admin rejecting employee's booking.
     */
    @GetMapping("/reject-booking/{booking_id}")
    public String rejectReservation(@PathVariable int booking_id) {
        try{
            bookingService.updateStatus(booking_id, BookingStatus.REJECTED);
            return "redirect:/home#overview";
        }catch (BookingNotFoundException e) {
            return "redirect:/home#myReservations?error=true&errorMessage=" + e.getMessage();
        }
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage (Model model){
        model.addAttribute(Constants.BODY_CONTENT, "access-denied");
        return "master-template";
    }

}
