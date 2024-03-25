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
        createParkingSlot(1, ParkingSlotType.COMPACT);
        createParkingSlot(2, ParkingSlotType.COMPACT);
        createParkingSlot(3, ParkingSlotType.REGULAR);
        createParkingSlot(4, ParkingSlotType.REGULAR);
        createParkingSlot(5, ParkingSlotType.REGULAR);
        createParkingSlot(6, ParkingSlotType.MOTORBIKE);
    }

    private void createParkingSlot(int slotNumber, ParkingSlotType type) {
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setAvailable(true);
        parkingSlot.setSlotNumber(slotNumber);
        parkingSlot.setType(type);
        parkingSlotRepository.save(parkingSlot);
    }
}
