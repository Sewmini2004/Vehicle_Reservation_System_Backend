package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BillingEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
import com.example.Vehicle_Reservation_System_Backend.utils.BillingConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<BillingDTO> searchPayments(String searchTerm) {
        List<BillingEntity> billingEntities = billingDao.searchPayments(searchTerm);
        return billingEntities.stream().map(BillingConverter::convertToDTO).collect(Collectors.toList());
    }




}
