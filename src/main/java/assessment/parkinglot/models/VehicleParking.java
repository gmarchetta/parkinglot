package assessment.parkinglot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A relationship betwen a vehicle and the parking slots where it will park. Parking slots is a list due to some
 * vehicles using more than one slot. It has checkin and checkout times for auditing
 */
@Entity
public class VehicleParking {
    @Id
    @GeneratedValue
    private UUID uuid;
    @ManyToOne
    private Vehicle vehicle;
    @ManyToMany
    private List<ParkingSlot> parkingSlots = new ArrayList<>();
    private Instant checkin;
    private Instant checkout;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public void addParkingSlot(ParkingSlot parkingSlot) {
        parkingSlot.setAvailable(false);
        this.parkingSlots.add(parkingSlot);
    }

    public Instant getCheckin() {
        return checkin;
    }

    public void setCheckin(Instant checkin) {
        this.checkin = checkin;
    }

    public Instant getCheckout() {
        return checkout;
    }

    public void setCheckout(Instant checkout) {
        this.checkout = checkout;
    }
}
