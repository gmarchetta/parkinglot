package assessment.parkinglot.services;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.repositories.ParkingSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class ParkingLotTestUtils {
    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    @BeforeEach
    public void initTests() {
        createParkingSlot(true, 1, ParkingSlotType.COMPACT);
        createParkingSlot(true, 2, ParkingSlotType.COMPACT);
        createParkingSlot(true, 3, ParkingSlotType.REGULAR);
        createParkingSlot(true, 4, ParkingSlotType.REGULAR);
        createParkingSlot(true, 5, ParkingSlotType.REGULAR);
        createParkingSlot(true, 6, ParkingSlotType.MOTORBIKE);
    }

    private void createParkingSlot(boolean available, int slotNumber, ParkingSlotType type) {
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setAvailable(true);
        parkingSlot.setSlotNumber(slotNumber);
        parkingSlot.setType(type);
        parkingSlotRepository.save(parkingSlot);
    }
}
