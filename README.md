# Parking lot api

## Description
This API allows to manage a parking lot, receiving and parking cars as they arrive, and signing them off when they leave.
It store information about when they were checked in, and when they were checked out for auditing and pricing (out of scope).

The API was implemented using spring boot and an H2 in memory database.

## Technical details
The data model includes a ParkingLot that models different locations that a company may own. For this sample API we ended
not using it, but left it in for the sake of completeness.

A few endpoints were implemented:
* /parkinglot/{id}/park: admit a vehicle into the parking lot. The Vehicle information is received in the request body.
* /parkinglot/{id}/signoff: a vehicle leaves the parking lot. The Vehicle information is received in the request body.
* /parkinglot/{id}/spots: returns a list of different parking lot spot types, with the amount of available spots
for each one of them
* /parkinglot/{id}/spots/available?vehicleType=CAR: returns true or false depending if there are available spots for the specified
vehicle type in the vehicleType query param

## Disclaimers
1) Tests for VehicleParkingService and VehicleService were deprioritized due to time constraints, since they are just passthroughs.
2) Tests were focused on the classes that added value to business: ParkingLotController, ParkingLotService and ParkingSlotService.
3) Tests for Repositories are not included since they belong to the JPA layer
4) Tests for domain models were not included since they do not add much value, and decided to leave them out for time
constraints.
