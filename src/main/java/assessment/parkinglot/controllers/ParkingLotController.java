package assessment.parkinglot.controllers;

import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.services.ParkingLotService;
import assessment.parkinglot.services.dtos.AvailableSpots;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles all REST calls to park a car, signoff a car, check the amount of available spots for a
 * vehicle type and get the amount of available spots, segregated by spot types
 */
@RestController
@Controller
@RequestMapping("parkinglot/{id}")
public class ParkingLotController {
    private ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * Park a vehicle. If the vehicle does not exist, it gets created. If the vehicle is already in the parking lot,
     * return a 400.
     *
     * @param vehicle to park
     * @return a 200, with the parking slot where the car was parked
     */
    @PostMapping("/park")
    private ResponseEntity<List<ParkingSlot>> parkVehicle(Vehicle vehicle) {
        return ResponseEntity.ok(parkingLotService.parkVehicle(vehicle));
    }

    /**
     * Sign offs a vehicle. If the vehicle does not exist, or is not currently parked in the parking lot, returns a
     * 400
     * @param vehicle leaving the parking lot
     */
    @PostMapping("/signoff")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void signoffVehicle(Vehicle vehicle) {
        parkingLotService.signoffVehicle(vehicle);
    }

    /**
     * Returns the amount of available parking slots to park, segregated by car type
     * @return list of spot types, with the amount of available spots for that type
     */
    @GetMapping("/spots")
    private ResponseEntity<List<AvailableSpots>> availableSpots() {
        return ResponseEntity.ok(parkingLotService.availableSpots());
    }

    /**
     * Returns a boolean indicating if there are available spots for the vehicle type received as query param
     * @param vehicleType for which we want to check if there are available spots
     * @return boolean representing if there are available spots for the vehicle type
     */
    @GetMapping("/spots/available")
    private ResponseEntity<Boolean> areSpotsAvailableFor(@RequestParam("vehicleType") VehicleType vehicleType) {
        return ResponseEntity.ok(parkingLotService.areSpotsAvailableFor(vehicleType));
    }
}
