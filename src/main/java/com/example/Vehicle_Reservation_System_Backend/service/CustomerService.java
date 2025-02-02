package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;

public interface CustomerService {
    public boolean addCustomer(CustomerDTO customer);
    public CustomerDTO getCustomerById(int customerId);
    public boolean updateCustomer(CustomerDTO customer);
    public boolean deleteCustomer(int customerId);
}
