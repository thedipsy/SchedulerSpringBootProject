package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;

import java.time.LocalDate;
@Data
public class BookingDto {

    private LocalDate bookedDate;
    private String email;
    private BookingStatus status;

    public BookingDto(LocalDate bookedDate, String email) {
        this.bookedDate = bookedDate;
        this.email = email;
        this.status = BookingStatus.PENDING;
    }

}
