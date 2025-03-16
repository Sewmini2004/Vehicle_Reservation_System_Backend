package com.example.Vehicle_Reservation_System_Backend.dao;

import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BillingEntity;

import java.sql.SQLException;
import java.util.List;

public interface BillingDao {
    BillingDTO getBillByBookingId(int bookingId) throws SQLException;
    boolean saveBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException;
    boolean updateBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException;
    public boolean deleteBill(int bookingId) throws SQLException;
    List<BillingDTO> getAllBills() throws SQLException;

    public List<BillingEntity> searchPayments(String searchTerm);
}
