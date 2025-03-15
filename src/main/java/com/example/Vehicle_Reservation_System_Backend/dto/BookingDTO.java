package com.example.Vehicle_Reservation_System_Backend.dto;

import java.util.Date;

public class BookingDTO {
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
    private BillingDTO billingDetails;

    private String customerName;
    private String driverName;
    private String vehicleModel;
    private String vehicleRegistrationNumber;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public BookingDTO() {}

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public BookingDTO(int bookingId, int customerId, int vehicleId, int driverId, String pickupLocation,
                      String dropLocation, Date bookingDate, String carType, double totalBill,
                      String cancelStatus, double distance,
                      String custoemrName, String driverName, String vehicleModel, String vehicleRegistrationNumber
    ) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.bookingDate = bookingDate;
        this.carType = carType;
        this.totalBill = totalBill;
        this.cancelStatus = cancelStatus;
        this.distance = distance;
        this.customerName = custoemrName;
        this.driverName = driverName;
        this.vehicleModel = vehicleModel;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public BookingDTO(int bookingId, int customerId, int vehicleId, int driverId, String pickupLocation,
                      String dropLocation, Date bookingDate, String carType, double totalBill,
                      String cancelStatus, double distance
    ) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.bookingDate = bookingDate;
        this.carType = carType;
        this.totalBill = totalBill;
        this.cancelStatus = cancelStatus;
        this.distance = distance;
    }

    public void setBillingDetails(BillingDTO billingDetails) {
        this.billingDetails = billingDetails;
    }

    public BillingDTO getBillingDetails() {
        return billingDetails;
    }
}
