package com.example.Vehicle_Reservation_System_Backend.service;


import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;

public interface CustomerService {
    public boolean addCustomer(CustomerEntity customer);
    public CustomerEntity getCustomerById(int customerId);
    public boolean updateCustomer(CustomerEntity customer);
    public boolean deleteCustomer(int customerId);
}
