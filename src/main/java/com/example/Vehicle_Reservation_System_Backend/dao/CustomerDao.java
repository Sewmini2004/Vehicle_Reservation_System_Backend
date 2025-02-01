package com.example.Vehicle_Reservation_System_Backend.dao;


import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;

import java.util.List;

public interface CustomerDao {
    boolean saveCustomer(CustomerEntity customerEntity);
    boolean updateCustomer(CustomerEntity customerEntity);
    boolean deleteCustomer(int id);
    List<CustomerEntity> getAllCustomers();
    CustomerEntity getById(int id);
}
