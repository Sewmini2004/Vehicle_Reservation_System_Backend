package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.DriverServiceImpl;

public class DriverServiceFactory {

    private static DriverService driverServiceInstance;

    public static DriverService getDriverService() {
        if (driverServiceInstance == null) {
            driverServiceInstance = new DriverServiceImpl();
        }
        return driverServiceInstance;
    }
}
