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
            String queryCheckExistence = "SELECT COUNT(*) FROM drivers WHERE license_number = ? OR phone_number = ?";
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
        String query = "INSERT INTO drivers (vehicle_id, name, license_number, status, shift_timing, salary, experience_years, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverEntity.getVehicleId());
            stmt.setString(2, driverEntity.getName());
            stmt.setString(3, driverEntity.getLicenseNumber());
            stmt.setString(4, driverEntity.getStatus());
            stmt.setString(5, driverEntity.getShiftTiming());
            stmt.setDouble(6, driverEntity.getSalary());
            stmt.setInt(7, driverEntity.getExperienceYears());
            stmt.setString(8, driverEntity.getPhoneNumber());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public DriverEntity getById(int driverId) {
        String query = "SELECT * FROM drivers WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DriverEntity(
                        rs.getInt("driver_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("name"),
                        rs.getString("license_number"),
                        rs.getString("status"),
                        rs.getString("shift_timing"),
                        rs.getDouble("salary"),
                        rs.getInt("experience_years"),
                        rs.getString("phone_number")
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
        String queryCheckExistence = "SELECT COUNT(*) FROM drivers WHERE driver_id = ?";
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
        String query = "UPDATE drivers SET vehicle_id = ?, name = ?, license_number = ?, status = ?, shift_timing = ?, salary = ?, experience_years = ?, phone_number = ? WHERE driver_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, driverEntity.getVehicleId());
            stmt.setString(2, driverEntity.getName());
            stmt.setString(3, driverEntity.getLicenseNumber());
            stmt.setString(4, driverEntity.getStatus());
            stmt.setString(5, driverEntity.getShiftTiming());
            stmt.setDouble(6, driverEntity.getSalary());
            stmt.setInt(7, driverEntity.getExperienceYears());
            stmt.setString(8, driverEntity.getPhoneNumber());
            stmt.setInt(9, driverEntity.getDriverId());
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
        String queryCheckExistence = "SELECT COUNT(*) FROM drivers WHERE driver_id = ?";
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
        String query = "DELETE FROM drivers WHERE driver_id = ?";
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
        String query = "SELECT * FROM drivers";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                drivers.add(new DriverEntity(
                        rs.getInt("driver_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("name"),
                        rs.getString("license_number"),
                        rs.getString("status"),
                        rs.getString("shift_timing"),
                        rs.getDouble("salary"),
                        rs.getInt("experience_years"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
}
