package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.BillingDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.BillingServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class BillingServiceFactory {

    // Private constructor to prevent instantiation (SRP)
    private BillingServiceFactory() {}

    // Method to create and return a database connection
    private static Connection createConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    // Method to create BillingDao
    public static BillingDao createBillingDao(Connection connection) {
        return new BillingDaoImpl(connection);
    }

    // Method to create BillingService with its dependencies
    public static BillingService createBillingService() throws SQLException {
        Connection connection = createConnection();

        // Return a new instance of BillingServiceImpl, passing the created BillingDao
        return new BillingServiceImpl(createBillingDao(connection));
    }
}
