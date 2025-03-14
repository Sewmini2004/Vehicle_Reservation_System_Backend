package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
import java.sql.SQLException;
import java.util.List;

public class BillingServiceImpl implements BillingService {

    private final BillingDao billingDao;

    public BillingServiceImpl(BillingDao billingDao) {
        this.billingDao = billingDao;
    }

    @Override
    public BillingDTO getBillByBookingId(int bookingId) throws SQLException {
        return billingDao.getBillByBookingId(bookingId);
    }

    @Override
    public List<BillingDTO> getAllBills() throws SQLException {
        return billingDao.getAllBills();
    }
}
