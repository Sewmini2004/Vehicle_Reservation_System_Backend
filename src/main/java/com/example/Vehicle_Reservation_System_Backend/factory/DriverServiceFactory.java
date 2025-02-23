package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.DriverDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.DriverDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.DriverServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;

public class DriverServiceFactory {

    private static DriverService driverServiceInstance;

    public static DriverService getDriverService() {
        if (driverServiceInstance == null) {
            Connection connection = DBConnection.getInstance().getConnection();
            DriverDao driverDao = new DriverDaoImpl(connection);
            driverServiceInstance = new DriverServiceImpl(driverDao);
        }
        return driverServiceInstance;
    }
}
