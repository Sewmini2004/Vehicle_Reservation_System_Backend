package com.example.Vehicle_Reservation_System_Backend.entity;

import java.util.Date;

public class BookingEntity {
    private int bookingId;
    private int customerId;
    private int vehicleId;
    private int driverId;
    private String pickupLocation;
    private String dropLocation;
    private Date bookingDate;
    private String carType;
    private double totalBill;

    // Default Constructor
    public BookingEntity() {}

    // Parameterized Constructor
    public BookingEntity(int bookingId, int customerId, int vehicleId, int driverId, String pickupLocation, String dropLocation, Date bookingDate, String carType, double totalBill) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.bookingDate = bookingDate;
        this.carType = carType;
        this.totalBill = totalBill;
    }

    // Constructor to convert from DTO
    public BookingEntity(com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO dto) {
        this.bookingId = dto.getBookingId();
        this.customerId = dto.getCustomerId();
        this.vehicleId = dto.getVehicleId();
        this.driverId = dto.getDriverId();
        this.pickupLocation = dto.getPickupLocation();
        this.dropLocation = dto.getDropLocation();
        this.bookingDate = dto.getBookingDate();
        this.carType = dto.getCarType();
        this.totalBill = dto.getTotalBill();
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public double getTotalBill() { return totalBill; }
    public void setTotalBill(double totalBill) { this.totalBill = totalBill; }

    // Convert to DTO
    public com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO toDTO() {
        return new com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO(
                this.bookingId,
                this.customerId,
                this.vehicleId,
                this.driverId,
                this.pickupLocation,
                this.dropLocation,
                this.bookingDate,
                this.carType,
                this.totalBill
        );
    }
}
