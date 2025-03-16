package com.example.Vehicle_Reservation_System_Backend.dto;

import java.sql.Timestamp;

public class BillingDTO {
    private int billId;
    private int bookingId;
    private double totalAmount;
    private double discountAmount;
    private double taxAmount;
    private double finalAmount;
    private String paymentMethod;
    private String paymentStatus;

    public int billId() {
        return billId;
    }

    public int bookingId() {
        return bookingId;
    }

    public double totalAmount() {
        return totalAmount;
    }

    public double discountAmount() {
        return discountAmount;
    }

    public double taxAmount() {
        return taxAmount;
    }

    public double finalAmount() {
        return finalAmount;
    }

    public String paymentMethod() {
        return paymentMethod;
    }

    public String paymentStatus() {
        return paymentStatus;
    }

    public Timestamp createdAt() {
        return createdAt;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    private Timestamp createdAt;

    public BillingDTO() {
    }

    public BillingDTO(int billId, int bookingId, double totalAmount, double discountAmount, double taxAmount, double finalAmount, String paymentMethod, String paymentStatus, Timestamp createdAt) {
        this.billId = billId;
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }
}
