package mk.ukim.finki.wp.schedulerspringbootproject.Model.Dto;

import lombok.Data;

@Data
public class DeskDto {

    private int ordinalNumber;
    private int officeId;

    public DeskDto(int ordinalNumber, int officeId) {
        this.ordinalNumber = ordinalNumber;
        this.officeId = officeId;
    }
}
