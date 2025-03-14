package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.VehicleDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.VehicleDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.VehicleServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class VehicleServiceFactory {

    private static VehicleService vehicleServiceInstance;

    public static VehicleService getVehicleService() throws SQLException {
        if (vehicleServiceInstance == null) {
            Connection connection = DBConnection.getInstance().getConnection();
            VehicleDao vehicleDao = new VehicleDaoImpl(connection);

            vehicleServiceInstance = new VehicleServiceImpl(vehicleDao);
        }
        return vehicleServiceInstance;
    }
}
