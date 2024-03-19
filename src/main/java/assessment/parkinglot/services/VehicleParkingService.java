package assessment.parkinglot.services;

import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleParking;
import assessment.parkinglot.repositories.VehicleParkingRepository;
import org.springframework.stereotype.Service;

/**
 * A service to perform operations with the relationship between Vehicles and Parking slots
 */
@Service
public class VehicleParkingService {
    private VehicleParkingRepository vehicleParkingRepository;

    public VehicleParkingService(VehicleParkingRepository vehicleParkingRepository) {
        this.vehicleParkingRepository = vehicleParkingRepository;
    }

    /**
     * Saves a vehicle parking instance to DB
     * @param vehicleParking
     * @return the persisted VehicleParking
     */
    public VehicleParking saveVehicleParking(VehicleParking vehicleParking) {
        return vehicleParkingRepository.save(vehicleParking);
    }

    /**
     * Finds a VehicleParking for a Vehicle currently parked in the parking lot
     * @param parkedVehicle vehicle that we want to find
     * @return a vehicle parking that has information about the vehicle parked (slot where it parked, checkin...)
     */
    public VehicleParking findByVehicleAndCheckedOutIsNull(Vehicle parkedVehicle) {
        return vehicleParkingRepository.findByVehicleAndCheckoutIsNull(parkedVehicle);
    }

    /**
     * Saves the Vehicle parking to DB
     * @param vehicleParking to save
     * @return the saved vehicle parking
     */
    public VehicleParking save(VehicleParking vehicleParking) {
        return vehicleParkingRepository.save(vehicleParking);
    }
}
