package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.VehicleDao;
import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDaoImpl implements VehicleDao {
    private Connection connection;

    // Constructor accepts the connection, used for testing with mocked connections
    public VehicleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveVehicle(VehicleEntity vehicleEntity) {
        String query = "INSERT INTO vehicle (carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicleEntity.getCarType());
            stmt.setString(2, vehicleEntity.getModel());
            stmt.setString(3, vehicleEntity.getAvailabilityStatus());
            stmt.setString(4, vehicleEntity.getRegistrationNumber());
            stmt.setString(5, vehicleEntity.getFuelType());
            stmt.setString(6, vehicleEntity.getCarModel());
            stmt.setInt(7, vehicleEntity.getSeatingCapacity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public VehicleEntity getById(int vehicleId) {
        String query = "SELECT * FROM vehicle WHERE vehicleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new VehicleEntity(
                        rs.getInt("vehicleId"),
                        rs.getString("carType"),
                        rs.getString("model"),
                        rs.getString("availabilityStatus"),
                        rs.getString("registrationNumber"),
                        rs.getString("fuelType"),
                        rs.getString("carModel"),
                        rs.getInt("seatingCapacity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<VehicleEntity> getAllVehicles() {
        List<VehicleEntity> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicle";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehicles.add(new VehicleEntity(
                        rs.getInt("vehicleId"),
                        rs.getString("carType"),
                        rs.getString("model"),
                        rs.getString("availabilityStatus"),
                        rs.getString("registrationNumber"),
                        rs.getString("fuelType"),
                        rs.getString("carModel"),
                        rs.getInt("seatingCapacity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    @Override
    public boolean updateVehicle(VehicleEntity vehicleEntity) {
        String query = "UPDATE vehicle SET carType = ?, model = ?, availabilityStatus = ?, registrationNumber = ?, fuelType = ?, carModel = ?, seatingCapacity = ? WHERE vehicleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicleEntity.getCarType());
            stmt.setString(2, vehicleEntity.getModel());
            stmt.setString(3, vehicleEntity.getAvailabilityStatus());
            stmt.setString(4, vehicleEntity.getRegistrationNumber());
            stmt.setString(5, vehicleEntity.getFuelType());
            stmt.setString(6, vehicleEntity.getCarModel());
            stmt.setInt(7, vehicleEntity.getSeatingCapacity());
            stmt.setInt(8, vehicleEntity.getVehicleId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteVehicle(int vehicleId) {
        String queryCheckExistence = "SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return false; // Vehicle not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Proceed to delete vehicle
        String query = "DELETE FROM vehicle WHERE vehicleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsById(int id) {
        String queryCheckExistence = "SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;  // Return true if vehicle exists, otherwise false
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
