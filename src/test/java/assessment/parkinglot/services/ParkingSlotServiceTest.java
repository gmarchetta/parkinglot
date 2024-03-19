package assessment.parkinglot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.services.dtos.AvailableSpots;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ParkingSlotServiceTest extends ParkingLotTestUtils {
    @Autowired
    private ParkingSlotService parkingSlotService;

    @Test
    public void testFindAvailableSpots_ThreeCompactThreeRegularOneMotorbike_Success() {
        List<AvailableSpots> availableSpotsList = parkingSlotService.findAvailableSpots();
        assertEquals(3, availableSpotsList.size());
        int compact = 0;
        int regular = 0;
        int motorbike = 0;

        for(AvailableSpots spots: availableSpotsList) {
            if(spots.getParkingSlotType().equals(ParkingSlotType.COMPACT)) {
                compact += spots.getAvailableSpots();
            } else if(spots.getParkingSlotType().equals(ParkingSlotType.REGULAR)) {
                regular += spots.getAvailableSpots();
            } else if(spots.getParkingSlotType().equals(ParkingSlotType.MOTORBIKE)) {
                motorbike += spots.getAvailableSpots();
            }
        }

        assertEquals(2, compact);
        assertEquals(3, regular);
        assertEquals(1, motorbike);
    }
}
