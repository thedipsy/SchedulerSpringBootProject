package mk.ukim.finki.wp.schedulerspringbootproject.Model.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Desk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deskId;

    private int ordinalNumber;

    @ManyToOne
    private Office office;

    @OneToOne (mappedBy="desk")
    private Employee employee;

    public Desk() {
    }

    public Desk(int ordinalNumber, Office office) {
        this.ordinalNumber = ordinalNumber;
        this.office = office;
    }

    public Desk(int ordinalNumber, Office office, Employee employee) {
        this.ordinalNumber = ordinalNumber;
        this.office = office;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "desk";
    }
}


