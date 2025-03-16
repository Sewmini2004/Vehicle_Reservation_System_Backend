package com.example.Vehicle_Reservation_System_Backend.entity;

import java.util.Date;

public class BookingEntity {
    private int bookingId;
    private int customerId;
    private String customerName;
    private int vehicleId;
    private String vehicleRegistrationNumber;
    private String vehicleModel;
    private int driverId;
    private String driverName;
    private String pickupLocation;
    private String dropLocation;
    private Date bookingDate;
    private String carType;
    private double totalBill;
    private String cancelStatus;
    private double distance;

    public int bookingId() {
        return bookingId;
    }

    public BookingEntity setBookingId(int bookingId) {
        this.bookingId = bookingId;
        return this;
    }

    public int customerId() {
        return customerId;
    }

    public BookingEntity setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public String customerName() {
        return customerName;
    }

    public BookingEntity setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public int vehicleId() {
        return vehicleId;
    }

    public BookingEntity setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public String vehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public BookingEntity setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        return this;
    }

    public String vehicleModel() {
        return vehicleModel;
    }

    public BookingEntity setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
        return this;
    }

    public int driverId() {
        return driverId;
    }

    public BookingEntity setDriverId(int driverId) {
        this.driverId = driverId;
        return this;
    }

    public String driverName() {
        return driverName;
    }

    public BookingEntity setDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    public String pickupLocation() {
        return pickupLocation;
    }

    public BookingEntity setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
        return this;
    }

    public String dropLocation() {
        return dropLocation;
    }

    public BookingEntity setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
        return this;
    }

    public Date bookingDate() {
        return bookingDate;
    }

    public BookingEntity setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
        return this;
    }

    public String carType() {
        return carType;
    }

    public BookingEntity setCarType(String carType) {
        this.carType = carType;
        return this;
    }

    public double totalBill() {
        return totalBill;
    }

    public BookingEntity setTotalBill(double totalBill) {
        this.totalBill = totalBill;
        return this;
    }

    public String cancelStatus() {
        return cancelStatus;
    }

    public BookingEntity setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
        return this;
    }

    public double distance() {
        return distance;
    }

    public BookingEntity setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    // Private constructor to prevent direct instantiation
    private BookingEntity(Builder builder) {
        this.bookingId = builder.bookingId;
        this.customerId = builder.customerId;
        this.vehicleId = builder.vehicleId;
        this.driverId = builder.driverId;
        this.pickupLocation = builder.pickupLocation;
        this.dropLocation = builder.dropLocation;
        this.bookingDate = builder.bookingDate;
        this.carType = builder.carType;
        this.totalBill = builder.totalBill;
        this.distance = builder.distance;
        this.cancelStatus = builder.cancelStatus;
        this.customerName = builder.customerName;
        this.driverName = builder.driverName;
        this.vehicleModel = builder.vehicleModel;
        this.vehicleRegistrationNumber = builder.vehicleRegistrationNumber;
    }

    public static class Builder {
        private int bookingId;
        private int customerId;
        private int vehicleId;
        private int driverId;
        private String pickupLocation;
        private String dropLocation;
        private Date bookingDate;
        private String carType;
        private double totalBill;
        private String cancelStatus;
        private double distance;
        private String customerName;
        private String driverName;
        private String vehicleModel;
        private String vehicleRegistrationNumber;

        // Setters for each field
        public Builder bookingId(int bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder customerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder vehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder driverId(int driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder pickupLocation(String pickupLocation) {
            this.pickupLocation = pickupLocation;
            return this;
        }

        public Builder dropLocation(String dropLocation) {
            this.dropLocation = dropLocation;
            return this;
        }

        public Builder bookingDate(Date bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public Builder carType(String carType) {
            this.carType = carType;
            return this;
        }

        public Builder totalBill(double totalBill) {
            this.totalBill = totalBill;
            return this;
        }

        public Builder cancelStatus(String cancelStatus) {
            this.cancelStatus = cancelStatus;
            return this;
        }

        public Builder distance(double distance) {
            this.distance = distance;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder driverName(String driverName) {
            this.driverName = driverName;
            return this;
        }

        public Builder vehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
            return this;
        }

        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
            return this;
        }

        // Build method to create the final object
        public BookingEntity build() {
            return new BookingEntity(this);
        }
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public String getCarType() {
        return carType;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public double getDistance() {
        return distance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }
}
