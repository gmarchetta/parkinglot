package assessment.parkinglot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

/**
 * A parking lot. It contains a list of Parking slots. For companies that have more than one parking lot available
 */
@Entity
public class ParkingLot {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String location;

    @OneToMany
    private List<ParkingSlot> parkingSlots;

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setParkingSlots(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }
}
