package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;

import java.sql.SQLException;

public class BillingDaoImpl implements BillingDao {
    @Override
    public BillingDTO getBillByBookingId(int bookingId) throws SQLException {
        return null;
    }

    @Override
    public boolean saveBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException {
        return false;
    }
}
