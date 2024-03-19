package assessment.parkinglot.repositories;

import assessment.parkinglot.models.Vehicle;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A Repository to interact with vehicles
 */
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Vehicle findByLicensePlate(String licensePlate);
}
