package assessment.parkinglot.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.services.ParkingLotService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit test to test the parking lot controller interface and make sure that the service is called as expected
 */
@WebMvcTest
public class ParkingLotControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ParkingLotService parkingLotService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(parkingLotService);
    }

    @Test
    public void testParkVehicle() throws Exception {
        List<ParkingSlot> parkingSlotList = new ArrayList<>();
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setId(UUID.randomUUID());
        parkingSlot.setSlotNumber(1);
        parkingSlotList.add(parkingSlot);

        doReturn(parkingSlotList).when(parkingLotService).parkVehicle(any(Vehicle.class));

        mockMvc.perform(post("/parkinglot/1/park")).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(parkingSlot.getId().toString())))
                .andExpect(jsonPath("$.[0].slotNumber", is(parkingSlot.getSlotNumber())));

        verify(parkingLotService, times(1)).parkVehicle(any(Vehicle.class));
    }

    @Test
    public void testSignoffVehicle() throws Exception {
        mockMvc.perform(post("/parkinglot/1/signoff")).andExpect(status().isNoContent());
        verify(parkingLotService, times(1)).signoffVehicle(any(Vehicle.class));
    }

    @Test
    public void testAvailableSpots() throws Exception {
        mockMvc.perform(get("/parkinglot/1/spots")).andExpect(status().isOk());
        verify(parkingLotService, times(1)).availableSpots();
    }

    @Test
    public void testAreAvailableSpotsFor() throws Exception {
        mockMvc.perform(get("/parkinglot/1/spots/available?vehicleType=CAR")).andExpect(status().isOk());
        verify(parkingLotService, times(1)).areSpotsAvailableFor(VehicleType.CAR);
    }
}
