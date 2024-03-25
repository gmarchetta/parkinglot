package assessment.parkinglot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import assessment.parkinglot.models.ParkingLot;
import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleParking;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.repositories.ParkingLotRepository;
import assessment.parkinglot.repositories.ParkingSlotRepository;
import assessment.parkinglot.repositories.VehicleParkingRepository;
import assessment.parkinglot.repositories.VehicleRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VehicleParkingServiceTest {
    @Autowired
    private VehicleParkingService vehicleParkingService;

    @Autowired
    private VehicleParkingRepository vehicleParkingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    public void testFindByVehicleAndCheckedOutIsNull_RecordsExists_ReturnsVehicleParking() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.CAR);
        vehicleRepository.save(vehicle);

        ParkingLot parkingLot = new ParkingLot();
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotNumber(1);
        parkingSlot.setType(ParkingSlotType.COMPACT);
        parkingSlot.setAvailable(true);
        List<ParkingSlot> parkingSlots = List.of(parkingSlot);
        parkingLot.setParkingSlots(parkingSlots);
        parkingLot.setLocation("Main Blvd");
        parkingLot.setName("Main Blvd");
        parkingSlotRepository.save(parkingSlot);
        parkingSlot.setParkingLot(parkingLot);
        parkingLotRepository.save(parkingLot);

        // vehicle hasn't parked yet
        assertNull(vehicleParkingService.findByVehicleAndCheckedOutIsNull(vehicle));

        VehicleParking vehicleParking = new VehicleParking();
        vehicleParking.setVehicle(vehicle);
        vehicleParking.setParkingSlots(parkingSlots);
        vehicleParking.setCheckin(Instant.now());
        vehicleParkingRepository.save(vehicleParking);

        // Vehicle already parked
        assertEquals(vehicleParking.getUuid(),
                vehicleParkingService.findByVehicleAndCheckedOutIsNull(vehicle).getUuid());
    }

    @Test
    public void testFindByVehicleAndCheckedOutIsNull_VehicleLeft_ReturnsNoVehicleParking() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.CAR);
        vehicleRepository.save(vehicle);

        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotNumber(1);
        parkingSlot.setType(ParkingSlotType.COMPACT);
        parkingSlot.setAvailable(true);
        parkingSlotRepository.save(parkingSlot);

        VehicleParking vehicleParking = new VehicleParking();
        vehicleParking.setVehicle(vehicle);
        vehicleParking.setParkingSlots(List.of(parkingSlot));
        vehicleParking.setCheckin(Instant.now());
        vehicleParking.setCheckout(Instant.now());
        vehicleParkingRepository.save(vehicleParking);

        // returns null since the vehicle already left (checkout is not null)
        assertNull(vehicleParkingService.findByVehicleAndCheckedOutIsNull(vehicle));
    }
}
