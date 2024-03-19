package assessment.parkinglot.repositories;

import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleParking;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository to interact with the relationship between vehicles and parking
 */
@Repository
public interface VehicleParkingRepository extends JpaRepository<VehicleParking, UUID> {
    VehicleParking findByVehicleAndCheckoutIsNull(Vehicle vehicle);
}
