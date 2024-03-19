package assessment.parkinglot.services;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.repositories.ParkingSlotRepository;
import assessment.parkinglot.services.dtos.AvailableSpots;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

/**
 * A service to perform operations on ParkingSlots
 */
@Service
public class ParkingSlotService {
    private ParkingSlotRepository parkingSlotRepository;

    public ParkingSlotService(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
    }

    /**
     * Checks if there are enough spots available for the specified slot type
     * @param slotType to check parking for
     * @param requiredParkingSlots amount of slots required
     * @return a boolean representing if there the required spots are available
     */
    public Boolean areSpotsAvailable(ParkingSlotType slotType, Integer requiredParkingSlots) {
        long availableSpots = parkingSlotRepository.countByTypeAndAvailable(slotType, true);
        return availableSpots >= requiredParkingSlots;
    }

    /**
     * Finds all available spots in the parking lot. They get grouped by Slot type
     * @return a list of available spots, grouped by their slot type and specifying the available quantity
     */
    public List<AvailableSpots> findAvailableSpots() {
        List<AvailableSpots> availableSpots = new ArrayList<>();
        for(ParkingSlotType parkingSlotType: ParkingSlotType.values()) {
            Long availableSpotsQuantity = parkingSlotRepository.countByTypeAndAvailable(parkingSlotType, true);
            if(availableSpotsQuantity > 0) {
                AvailableSpots spots = new AvailableSpots();
                spots.setParkingSlotType(parkingSlotType);
                spots.setAvailableSpots(availableSpotsQuantity);
                availableSpots.add(spots);
            }
        }

        return availableSpots;
    }

    /**
     * Persists all parking slots
     * @param parkingSlots to persist
     */
    public void saveAll(List<ParkingSlot> parkingSlots) {
        parkingSlotRepository.saveAll(parkingSlots);
    }

    /**
     * Looks for parking slots for the specified parking slot types, and filtering by availability and required parking
     * spots
     * @param parkingSlotFor the parking slot type for which we are looking for parking slots
     * @param available if the spots should be available or not
     * @param requiredParkingSlots how many spots we require
     * @return a list of parking slots (available or not depending on filtering)
     */
    public List<ParkingSlot> findByParkingSlotTypeInAndAvailableLimitedTo(List<ParkingSlotType> parkingSlotFor,
            boolean available, Integer requiredParkingSlots) {
        return parkingSlotRepository.findByTypeInAndAvailable(parkingSlotFor, available, Limit.of(requiredParkingSlots));
    }
}
