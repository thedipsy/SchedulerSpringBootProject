package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int officeId;

    private int ordinalNumber;

    @OneToMany(mappedBy = "office", fetch = FetchType.LAZY, cascade={CascadeType.REMOVE})
    private List<Desk> deskList;

    public Office() {}

    public Office(int ordinalNumber) {
        this.ordinalNumber = ordinalNumber;
        this.deskList = new ArrayList<>();
    }

}
