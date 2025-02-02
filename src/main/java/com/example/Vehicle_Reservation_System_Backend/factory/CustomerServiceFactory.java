package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.CustomerServiceImpl;

public class CustomerServiceFactory {

    public static CustomerService getCustomerService() {
        return new CustomerServiceImpl();
    }
}
