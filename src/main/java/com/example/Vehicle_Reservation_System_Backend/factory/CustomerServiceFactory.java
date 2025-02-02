package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.CustomerServiceImpl;

public class CustomerServiceFactory {
    // Singleton instance of CustomerService
    private static CustomerService customerServiceInstance;

    public static CustomerService getCustomerService() {
        if (customerServiceInstance == null) {
            customerServiceInstance = new CustomerServiceImpl();
        }
        return customerServiceInstance;
    }
}
