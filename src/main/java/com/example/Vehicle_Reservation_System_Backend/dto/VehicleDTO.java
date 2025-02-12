package com.example.Vehicle_Reservation_System_Backend.dto;

public class VehicleDTO {
    private int vehicleId;
    private String carType;
    private String model;
    private String availabilityStatus;
    private String registrationNumber;
    private String fuelType;
    private String carModel;
    private int seatingCapacity;

    public VehicleDTO(int vehicleId, String carType, String model, String availabilityStatus, String registrationNumber, String fuelType, String carModel, int seatingCapacity) {
        this.vehicleId = vehicleId;
        this.carType = carType;
        this.model = model;
        this.availabilityStatus = availabilityStatus;
        this.registrationNumber = registrationNumber;
        this.fuelType = fuelType;
        this.carModel = carModel;
        this.seatingCapacity = seatingCapacity;
    }

    public VehicleDTO() {
    }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public int getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(int seatingCapacity) { this.seatingCapacity = seatingCapacity; }
}
