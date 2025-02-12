package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.VehicleServiceImpl;

public class VehicleServiceFactory {

    private static VehicleService vehicleServiceInstance;

    public static VehicleService getVehicleService() {
        if (vehicleServiceInstance == null) {
            vehicleServiceInstance = new VehicleServiceImpl();
        }
        return vehicleServiceInstance;
    }
}
