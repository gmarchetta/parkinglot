package assessment.parkinglot.services.dtos;

import assessment.parkinglot.models.ParkingSlotType;

/**
 * A dto to return the Available Spots in the parking lot. It groups parking slots by parking slot type, and assigns
 * the amount of available spots for that parking slot type
 */
public class AvailableSpots {
    private ParkingSlotType parkingSlotType;
    private Long availableSpots;

    public ParkingSlotType getParkingSlotType() {
        return parkingSlotType;
    }

    public void setParkingSlotType(ParkingSlotType parkingSlotType) {
        this.parkingSlotType = parkingSlotType;
    }

    public Long getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Long availableSpots) {
        this.availableSpots = availableSpots;
    }
}
