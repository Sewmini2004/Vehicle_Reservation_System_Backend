package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.*;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.*;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.BookingServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;


public class BookingServiceFactory {

    // Private constructor to prevent instantiation (SRP)
    private BookingServiceFactory() {}

    private static Connection createConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
    public static BookingDao createBookingDao(Connection connection) {
        return new BookingDaoImpl(connection);
    }

    public static CustomerDao createCustomerDao(Connection connection) {
        return new CustomerDaoImpl(connection);
    }
    public static DriverDao createDriverDao(Connection connection) {
        return new DriverDaoImpl(connection);
    }
    public static VehicleDao createVehicleDao(Connection connection) {
        return new VehicleDaoImpl(connection);
    }


    public static BillingDao createBillingDao(Connection connection) {
        return new BillingDaoImpl(connection);
    }


    public static BookingService createBookingService() throws SQLException {
        Connection connection = createConnection();

        return new BookingServiceImpl(
                createBookingDao(connection),
                createCustomerDao(connection),
                createDriverDao(connection),
                createVehicleDao(connection),
                createBillingDao(connection)
        );
    }
}
