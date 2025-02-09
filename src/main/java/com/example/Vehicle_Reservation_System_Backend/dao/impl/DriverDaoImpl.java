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

    public DriverDaoImpl() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public boolean saveDriver(DriverEntity driverEntity) {
        // Check if the license number or phone number already exists
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

        // Insert the driver into the database
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
        String query = "SELECT * FROM driver WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DriverEntity(
                        rs.getInt("driverId"),
                        rs.getString("name"),
                        rs.getString("licenseNumber"),
                        rs.getString("status"),
                        rs.getString("shiftTiming"),
                        rs.getDouble("salary"),
                        rs.getInt("experienceYears"),
                        rs.getString("phoneNumber")
                );
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
        // Check if the driver exists before updating
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

        // Proceed to update the driver
        String query = "UPDATE driver SET  name = ?, licenseNumber = ?, status = ?, shiftTiming = ?, salary = ?, experienceYears = ?, phoneNumber = ? WHERE driverId = ?";
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
        // Check if the driver exists before deleting
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

        // Proceed to delete the driver
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
        List<DriverEntity> drivers = new ArrayList<>();
        String query = "SELECT * FROM driver";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                drivers.add(new DriverEntity(
                        rs.getInt("driverId"),
                        rs.getString("name"),
                        rs.getString("licenseNumber"),
                        rs.getString("status"),
                        rs.getString("shiftTiming"),
                        rs.getDouble("salary"),
                        rs.getInt("experienceYears"),
                        rs.getString("phoneNumber")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
}
