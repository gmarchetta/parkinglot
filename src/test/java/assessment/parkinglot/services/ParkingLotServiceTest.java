package assessment.parkinglot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.repositories.ParkingLotRepository;
import assessment.parkinglot.repositories.ParkingSlotRepository;
import assessment.parkinglot.repositories.VehicleParkingRepository;
import assessment.parkinglot.repositories.VehicleRepository;
import assessment.parkinglot.services.dtos.AvailableSpots;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class ParkingLotServiceTest extends ParkingLotTestUtils {
    @Autowired
    ParkingLotService parkingLotService;

    @Autowired
    ParkingSlotRepository parkingSlotRepository;

    @Autowired
    VehicleParkingRepository vehicleParkingRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @AfterEach
    public void tearDown() {
        vehicleParkingRepository.deleteAll();
        parkingLotRepository.deleteAll();
        parkingSlotRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void testParkCar_AvailableSlot_Success() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);

        List<ParkingSlot> parkedInSlots = parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());
        assertEquals(1, parkedInSlots.size());
    }

    @Test
    public void testParkThreeCars_AvailableSlots_Success() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);
        List<ParkingSlot> parkedInSlots = parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());
        assertEquals(1, parkedInSlots.size());

        vehicle = new Vehicle();
        vehicle.setLicensePlate("AAC-890");
        vehicle.setType(VehicleType.CAR);
        parkedInSlots = parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());
        assertEquals(1, parkedInSlots.size());

        vehicle = new Vehicle();
        vehicle.setLicensePlate("AAD-890");
        vehicle.setType(VehicleType.CAR);
        parkedInSlots = parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());
        assertEquals(1, parkedInSlots.size());
    }

    @Test
    public void testParkOneVan_AvailableSlots_Success() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.VAN);

        List<ParkingSlot> parkedInSlots = parkingLotService.parkVehicle(vehicle);
        assertFalse(parkingLotService.areSpotsAvailableFor(VehicleType.VAN));
        assertEquals(3, parkedInSlots.size());
    }

    @Test
    public void testParkTwoVans_NoAvailableSlots_Fails() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.VAN);

        parkingLotService.parkVehicle(vehicle);
        assertFalse(parkingLotService.areSpotsAvailableFor(VehicleType.VAN));

        vehicle = new Vehicle();
        vehicle.setLicensePlate("AAC-890");
        vehicle.setType(VehicleType.VAN);

        try {
            parkingLotService.parkVehicle(vehicle);
        } catch(ResponseStatusException e) {
            assertEquals("Not enough Parking Slots for vehicle", e.getReason());
            assertEquals(400, e.getStatusCode().value());
            return;
        }

        fail();
    }

    @Test
    public void testParkCar_AlreadyParked_Fails() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);

        parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());

        try {
            parkingLotService.parkVehicle(vehicle);
        } catch(ResponseStatusException e) {
            assertEquals("Car is already in the parking lot", e.getReason());
            assertEquals(400, e.getStatusCode().value());
            return;
        }

        fail();
    }

    @Test
    public void testSignoffCar_CarWarParked_Success() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);

        List<ParkingSlot> parkedInSlots = parkingLotService.parkVehicle(vehicle);
        vehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate());
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle).getCheckout());
        for(ParkingSlot parkedInSlot : parkedInSlots) {
            assertFalse(parkingSlotRepository.findById(parkedInSlot.getId()).get().isAvailable());
        }

        parkingLotService.signoffVehicle(vehicle);
        assertNull(vehicleParkingRepository.findByVehicleAndCheckoutIsNull(vehicle));
        for(ParkingSlot parkedInSlot : parkedInSlots) {
            assertTrue(parkingSlotRepository.findById(parkedInSlot.getId()).get().isAvailable());
        }
    }

    @Test
    public void testSignoffCar_CarDoesNotExist_Fails() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);

        try {
            parkingLotService.signoffVehicle(vehicle);
        } catch(ResponseStatusException e) {
            assertEquals("Vehicle does not exist", e.getReason());
            assertEquals(400, e.getStatusCode().value());
            return;
        }

        fail();
    }

    @Test
    public void testSignoffCar_CarNotParkedAtParkingLot_Fails() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.CAR);
        vehicleRepository.save(vehicle);

        try {
            parkingLotService.signoffVehicle(vehicle);
        } catch(ResponseStatusException e) {
            assertEquals("Vehicle is not at the parking lot", e.getReason());
            assertEquals(400, e.getStatusCode().value());
            return;
        }

        fail();
    }

    @Test
    public void areSpotsAvailableForMotorbike_SpotsAvailable_ReturnsTrue() {
        assertTrue(parkingLotService.areSpotsAvailableFor(VehicleType.MOTORBIKE));
    }

    @Test
    public void areSpotsAvailableForMotorbike_SpotsNotAvailable_ReturnsFalse() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.MOTORBIKE);
        parkingLotService.parkVehicle(vehicle);
        assertFalse(parkingLotService.areSpotsAvailableFor(VehicleType.MOTORBIKE));
    }

    @Test
    public void areSpotsAvailableForVan_SpotsAvailable_ReturnsTrue() {
        assertTrue(parkingLotService.areSpotsAvailableFor(VehicleType.VAN));
    }

    @Test
    public void areSpotsAvailableForVan_SpotsNotAvailable_ReturnsFalse() {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("AAB-890");
        vehicle.setType(VehicleType.VAN);
        parkingLotService.parkVehicle(vehicle);
        assertFalse(parkingLotService.areSpotsAvailableFor(VehicleType.VAN));
    }

    @Test
    public void testAvailableSpots_ReturnsListOfAvailableSpots() {
        List<AvailableSpots> availableSpots = parkingLotService.availableSpots();
        AvailableSpots availableCompactSpots = new AvailableSpots();
        availableCompactSpots.setAvailableSpots(2l);
        availableCompactSpots.setParkingSlotType(ParkingSlotType.COMPACT);

        AvailableSpots availableRegularSpots = new AvailableSpots();
        availableRegularSpots.setAvailableSpots(3l);
        availableRegularSpots.setParkingSlotType(ParkingSlotType.REGULAR);

        AvailableSpots availableMotorbikeSpots = new AvailableSpots();
        availableMotorbikeSpots.setAvailableSpots(1l);
        availableMotorbikeSpots.setParkingSlotType(ParkingSlotType.MOTORBIKE);

        assertTrue(availableSpots.stream().
            filter(p -> p.getAvailableSpots().equals(2l)).
            filter(p -> p.getParkingSlotType().equals(ParkingSlotType.COMPACT)).
            findFirst().isPresent());

        assertTrue(availableSpots.stream().
                filter(p -> p.getAvailableSpots().equals(3l)).
                filter(p -> p.getParkingSlotType().equals(ParkingSlotType.REGULAR)).
                findFirst().isPresent());

        assertTrue(availableSpots.stream().
                filter(p -> p.getAvailableSpots().equals(1l)).
                filter(p -> p.getParkingSlotType().equals(ParkingSlotType.MOTORBIKE)).
                findFirst().isPresent());
    }
}
