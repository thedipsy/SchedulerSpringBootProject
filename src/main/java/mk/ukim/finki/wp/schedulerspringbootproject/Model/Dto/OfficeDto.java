package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;

@Data
public class OfficeDto {

    private int ordinalNumber;

    public OfficeDto(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
    }

}
