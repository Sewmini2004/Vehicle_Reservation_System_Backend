package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.DriverDao;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDaoImpl implements DriverDao {

    private Connection connection;

    public DriverDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveDriver(DriverEntity driverEntity) {
        connection = DBConnection.getInstance().getConnection();

        try {
            String queryCheckExistence = "SELECT COUNT(*) FROM driver WHERE licenseNumber = ? OR phoneNumber = ?";
            PreparedStatement stmt = connection.prepareStatement(queryCheckExistence);
            stmt.setString(1, driverEntity.getLicenseNumber());
            stmt.setString(2, driverEntity.getPhoneNumber());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new AlreadyException("Driver with the same license number or phone number already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String query = "INSERT INTO driver (name, licenseNumber, status, shiftTiming, salary, experienceYears, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, driverEntity.getName());
            stmt.setString(2, driverEntity.getLicenseNumber());
            stmt.setString(3, driverEntity.getStatus());
            stmt.setString(4, driverEntity.getShiftTiming());
            stmt.setDouble(5, driverEntity.getSalary());
            stmt.setInt(6, driverEntity.getExperienceYears());
            stmt.setString(7, driverEntity.getPhoneNumber());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public DriverEntity getById(int driverId) {
        connection = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM driver WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new DriverEntity.Builder()
                        .driverId(rs.getInt("driverId"))
                        .name(rs.getString("name"))
                        .licenseNumber(rs.getString("licenseNumber"))
                        .status(rs.getString("status"))
                        .shiftTiming(rs.getString("shiftTiming"))
                        .salary(rs.getDouble("salary"))
                        .experienceYears(rs.getInt("experienceYears"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .build();
            } else {
                throw new NotFoundException("Driver with ID " + driverId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateDriver(DriverEntity driverEntity) {
        connection = DBConnection.getInstance().getConnection();
        String queryCheckExistence = "SELECT COUNT(*) FROM driver WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, driverEntity.getDriverId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                throw new NotFoundException("Driver with ID " + driverEntity.getDriverId() + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String query = "UPDATE driver SET name = ?, licenseNumber = ?, status = ?, shiftTiming = ?, salary = ?, experienceYears = ?, phoneNumber = ? WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, driverEntity.getName());
            stmt.setString(2, driverEntity.getLicenseNumber());
            stmt.setString(3, driverEntity.getStatus());
            stmt.setString(4, driverEntity.getShiftTiming());
            stmt.setDouble(5, driverEntity.getSalary());
            stmt.setInt(6, driverEntity.getExperienceYears());
            stmt.setString(7, driverEntity.getPhoneNumber());
            stmt.setInt(8, driverEntity.getDriverId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDriver(int driverId) {
        connection = DBConnection.getInstance().getConnection();
        String queryCheckExistence = "SELECT COUNT(*) FROM driver WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                throw new NotFoundException("Driver with ID " + driverId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String query = "DELETE FROM driver WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DriverEntity> getAllDrivers() {
        connection = DBConnection.getInstance().getConnection();
        List<DriverEntity> drivers = new ArrayList<>();
        String query = "SELECT * FROM driver";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                drivers.add(new DriverEntity.Builder()
                        .driverId(rs.getInt("driverId"))
                        .name(rs.getString("name"))
                        .licenseNumber(rs.getString("licenseNumber"))
                        .status(rs.getString("status"))
                        .shiftTiming(rs.getString("shiftTiming"))
                        .salary(rs.getDouble("salary"))
                        .experienceYears(rs.getInt("experienceYears"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    @Override
    public boolean existsById(int id) {
        connection = DBConnection.getInstance().getConnection();
        String queryCheckExistence = "SELECT COUNT(*) FROM driver WHERE driverId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
