package assessment.parkinglot.repositories;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository to interact with parking slots
 */
@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {
    Long countByTypeAndAvailable(ParkingSlotType vehicleType, Boolean available);

    List<ParkingSlot> findByTypeInAndAvailable(List<ParkingSlotType> parkingSlotTypes, boolean available, Limit of);
}
