package assessment.parkinglot.services;

import assessment.parkinglot.models.ParkingLot;
import assessment.parkinglot.models.ParkingSlot;
import assessment.parkinglot.models.ParkingSlotType;
import assessment.parkinglot.models.Vehicle;
import assessment.parkinglot.models.VehicleParking;
import assessment.parkinglot.models.VehicleType;
import assessment.parkinglot.services.dtos.AvailableSpots;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * A service to perform operations on a parking lot
 */
@Service
public class ParkingLotService {
    private ParkingSlotService parkingSlotService;
    private VehicleService vehicleService;
    private VehicleParkingService vehicleParkingService;

    public ParkingLotService(ParkingSlotService parkingSlotService, VehicleService vehicleService,
            VehicleParkingService vehicleParkingService) {
        this.parkingSlotService = parkingSlotService;
        this.vehicleService = vehicleService;
        this.vehicleParkingService = vehicleParkingService;
    }

    /**
     * Parks a vehicle in the parking lot. If the vehicle does not exist it gets created. If the vehicle is already
     * parked a 400 will be thrown. If no available slot for the vehicle type, a 400 is thrown
     * @param vehicle to park
     * @return a list of the parking slot where the vehicle was parked
     */
    @Transactional
    public List<ParkingSlot> parkVehicle(Vehicle vehicle) {
        Vehicle result = vehicleService.findByLicensePlate(vehicle.getLicensePlate());
        if(result == null) {
            result = vehicleService.createVehicle(vehicle);
        }

        if(vehicleParkingService.findByVehicleAndCheckedOutIsNull(result) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is already in the parking lot");
        }

        List<ParkingSlot> availableParkingSlots = parkingSlotService.findByParkingSlotTypeInAndAvailableLimitedTo(
                getParkingSlotFor(vehicle.getType()), true, vehicle.getType().getRequiredParkingSlots());
        if(vehicle.getType().getRequiredParkingSlots() > availableParkingSlots.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough Parking Slots for vehicle");
        }

        VehicleParking vehicleParking = new VehicleParking();
        vehicleParking.setVehicle(result);
        vehicleParking.setCheckin(Instant.now());

        for(ParkingSlot slot: availableParkingSlots) {
            vehicleParking.addParkingSlot(slot);
        }

        parkingSlotService.saveAll(availableParkingSlots);
        vehicleParkingService.save(vehicleParking);
        return availableParkingSlots;
    }

    /**
     * A vehicle leaves the parking lot. If it does not exist or if it's not at the parking lot, a 400 is returned
     * @param vehicle that leaves the parking lot
     */
    @Transactional
    public void signoffVehicle(Vehicle vehicle) {
        Vehicle parkedVehicle = vehicleService.findByLicensePlate(vehicle.getLicensePlate());
        if(parkedVehicle == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle does not exist");
        }

        VehicleParking vehicleParking = vehicleParkingService.findByVehicleAndCheckedOutIsNull(parkedVehicle);
        if(vehicleParking == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle is not at the parking lot");
        }

        vehicleParking.setCheckout(Instant.now());

        for(ParkingSlot slot: vehicleParking.getParkingSlots()) {
            slot.setAvailable(true);
        }

        vehicleParkingService.save(vehicleParking);
    }

    /**
     * Checks if there are available spots to park a car of the specified type
     * @param vehicleType to check available spots for
     * @return a boolean telling if there are spots for the vehicle type
     */
    public Boolean areSpotsAvailableFor(VehicleType vehicleType) {
        List<ParkingSlotType> slotTypes = getParkingSlotFor(vehicleType);
        Boolean areSpotsAvailable = false;
        for(ParkingSlotType slotType: slotTypes) {
            if(parkingSlotService.areSpotsAvailable(slotType, vehicleType.getRequiredParkingSlots())) {
                areSpotsAvailable = true;
                break;
            }
        }
        return areSpotsAvailable;
    }

    /**
     * Returns a list of the amount of available spots, grouped by Parking Slot type
     * @return a list of the amount of available spots, grouped by Parking Slot type
     */
    public List<AvailableSpots> availableSpots() {
        return parkingSlotService.findAvailableSpots();
    }

    // Returns the list of Parking Slot Types where the specified vehicle type can park
    private List<ParkingSlotType> getParkingSlotFor(VehicleType vehicleType) {
        switch(vehicleType) {
            case VAN:
                return List.of(ParkingSlotType.REGULAR);
            case CAR:
                return List.of(ParkingSlotType.REGULAR, ParkingSlotType.COMPACT);
            case MOTORBIKE:
                return List.of(ParkingSlotType.MOTORBIKE);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown Vehicle Type");
        }
    }
}
