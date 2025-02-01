package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.CustomerDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public boolean addCustomer(CustomerEntity customer) {
        return customerDao.saveCustomer(customer);
    }

    @Override
    public CustomerEntity getCustomerById(int customerId) {
        return customerDao.getById(customerId);
    }

    @Override
    public boolean updateCustomer(CustomerEntity customer) {
        return customerDao.updateCustomer(customer);
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        return customerDao.deleteCustomer(customerId);
    }
}
