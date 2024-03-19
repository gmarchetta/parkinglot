package assessment.parkinglot.services;

import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

/**
 * A service to perform operations with vehicles
 */
@Service
public class VehicleService {
    private VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Finds a persisted vehicle by license plate
     * @param licensePlate for the vehicle we want to find
     * @return
     */
    public Vehicle findByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    /**
     * Persists a vehicle in DB
     * @param vehicle to save to DB
     * @return the created vehicle
     */
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
