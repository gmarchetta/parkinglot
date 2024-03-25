package assessment.parkinglot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.repositories.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VehicleServiceTest {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    public void testFindByLicensePlate_VehicleExists_ReturnsVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.CAR);
        vehicle.setLicensePlate("UFD-123");
        vehicleRepository.save(vehicle);

        Vehicle result = vehicleService.findByLicensePlate(vehicle.getLicensePlate());
        assertEquals(vehicle.getVehicleId(), result.getVehicleId());
    }

    @Test
    public void testFindByLicensePlate_VehicleDoesNotExist_ReturnsVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.CAR);
        vehicle.setLicensePlate("UFD-123");
        vehicleRepository.save(vehicle);

        Vehicle result = vehicleService.findByLicensePlate("UFD-122");
        assertNull(result);
    }
}
