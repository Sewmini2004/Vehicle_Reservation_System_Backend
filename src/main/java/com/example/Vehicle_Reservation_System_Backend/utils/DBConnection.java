package com.example.Vehicle_Reservation_System_Backend.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() {
        System.out.println("LOG::DBConnection::Constructor:Started");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vehicle_reservation?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "1234"
            );
        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("LOG::DBConnection::Constructor:An error occured while creating db connection.\nmessage: "+throwables.getMessage());
            throwables.printStackTrace();

        }
        System.out.println("LOG::DBConnection::Constructor:end");
    }

    public static DBConnection getInstance() {
        try {
            if (dbConnection == null || dbConnection.getConnection().isClosed()) {
                dbConnection = new DBConnection();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
