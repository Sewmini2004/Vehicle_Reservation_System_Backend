package com.example.Vehicle_Reservation_System_Backend.dao;


import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDao {
    boolean saveCustomer(CustomerEntity customerEntity) throws SQLException;
    boolean updateCustomer(CustomerEntity customerEntity);
    boolean deleteCustomer(int id);
    List<CustomerEntity> getAllCustomers();
    CustomerEntity getById(int id);
    boolean existsById(int id);
    public List<CustomerEntity> searchCustomers(String searchTerm);
}
