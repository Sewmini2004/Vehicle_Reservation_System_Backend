package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import java.sql.SQLException;
import java.util.List;

public interface BillingService {
    BillingDTO getBillByBookingId(int bookingId) throws SQLException;
    List<BillingDTO> getAllBills() throws SQLException;
}
