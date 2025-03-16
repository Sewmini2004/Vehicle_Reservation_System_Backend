package com.example.Vehicle_Reservation_System_Backend.entity;

public class DriverEntity {

    private int driverId;
    private String name;
    private String licenseNumber;
    private String status;
    private String shiftTiming;
    private double salary;
    private int experienceYears;
    private String phoneNumber;

    private DriverEntity(Builder builder) {
        this.driverId = builder.driverId;
        this.name = builder.name;
        this.licenseNumber = builder.licenseNumber;
        this.status = builder.status;
        this.shiftTiming = builder.shiftTiming;
        this.salary = builder.salary;
        this.experienceYears = builder.experienceYears;
        this.phoneNumber = builder.phoneNumber;
    }

    public static class Builder {
        private int driverId;
        private String name;
        private String licenseNumber;
        private String status;
        private String shiftTiming;
        private double salary;
        private int experienceYears;
        private String phoneNumber;

        public Builder driverId(int driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder shiftTiming(String shiftTiming) {
            this.shiftTiming = shiftTiming;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder experienceYears(int experienceYears) {
            this.experienceYears = experienceYears;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public DriverEntity build() {
            return new DriverEntity(this);
        }
    }

    // Getters
    public int getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getShiftTiming() {
        return shiftTiming;
    }

    public double getSalary() {
        return salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
