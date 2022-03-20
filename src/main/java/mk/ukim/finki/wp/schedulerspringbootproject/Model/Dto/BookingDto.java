package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;
import mk.ukim.finki.wp.schedulerspringbootproject.Model.Enumetarion.BookingStatus;

import java.util.Date;

@Data
public class BookingDto {

    private Date bookedDate;
    private String email;
    private BookingStatus status;

    public BookingDto(Date bookedDate, String email) {
        this.bookedDate = bookedDate;
        this.email = email;
        this.status = BookingStatus.CREATED;
    }

}
