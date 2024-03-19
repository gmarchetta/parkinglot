package assessment.parkinglot.models;

public enum VehicleType {
    CAR(1), VAN(3), MOTORBIKE(1);

    private Integer requiredParkingSlots;

    VehicleType(Integer requiredParkingSlots) {
        this.requiredParkingSlots = requiredParkingSlots;
    }

    public Integer getRequiredParkingSlots() {
        return requiredParkingSlots;
    }
}
