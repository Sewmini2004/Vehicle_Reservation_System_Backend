package com.example.Vehicle_Reservation_System_Backend.entity;

public class DriverEntity{
    private int driverId;
    private int vehicleId;
    private String name;
    private String licenseNumber;
    private String status;
    private String shiftTiming;
    private double salary;
    private int experienceYears;
    private String phoneNumber;

    public DriverEntity(int driverId, int vehicleId, String name, String licenseNumber, String status, String shiftTiming, double salary, int experienceYears, String phoneNumber) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.status = status;
        this.shiftTiming = shiftTiming;
        this.salary = salary;
        this.experienceYears = experienceYears;
        this.phoneNumber = phoneNumber;
    }

    public DriverEntity() {
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShiftTiming() {
        return shiftTiming;
    }

    public void setShiftTiming(String shiftTiming) {
        this.shiftTiming = shiftTiming;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
