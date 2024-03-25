package assessment.parkinglot.repositories;

import assessment.parkinglot.models.ParkingLot;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, UUID> {

}
