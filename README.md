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

## Outside of scope
* An authentication mechanism will be required so only authorized users can perform operations with the API. OAuth 2 would be a
good option. Roles could also be introduced in case we wanted to offer customers features like loyalty points, know where they 
parked, show the checkin time, biometric checkin and checkout and so on
* If the service is expected to be deployed to the cloud as part of a microservice architecture, a gateway would be desireable to
expose a simple interface to the external world and hide our internals
