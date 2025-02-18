package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {
    public boolean addCustomer(CustomerDTO customer) throws SQLException;
    public CustomerDTO getCustomerById(int customerId);
    public boolean existsById(int customerId);
    public boolean updateCustomer(CustomerDTO customer);
    public boolean deleteCustomer(int customerId);
    List<CustomerDTO> getAllCustomers();

}
