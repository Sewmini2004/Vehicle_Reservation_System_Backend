package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.CustomerDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.CustomerServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;

public class CustomerServiceFactory {
    // Singleton instance of CustomerService
    private static CustomerService customerServiceInstance;

    public static CustomerService getCustomerService() {
        if (customerServiceInstance == null) {
            Connection connection = DBConnection.getInstance().getConnection();
            CustomerDao customerDao = new CustomerDaoImpl(connection);
            customerServiceInstance = new CustomerServiceImpl(customerDao);
        }
        return customerServiceInstance;
    }
}
