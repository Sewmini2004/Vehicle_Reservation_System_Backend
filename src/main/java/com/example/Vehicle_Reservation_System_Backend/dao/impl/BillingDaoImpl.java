package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;

import java.sql.*;

public class BillingDaoImpl implements BillingDao {

    private final Connection connection;

    // Constructor to accept shared Connection instance
    public BillingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public BillingDTO getBillByBookingId(int bookingId) throws SQLException {
        String query = "SELECT * FROM Billing WHERE bookingId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new BillingDTO(
                        resultSet.getInt("billId"),
                        resultSet.getInt("bookingId"),
                        resultSet.getDouble("totalAmount"),
                        resultSet.getDouble("discountAmount"),
                        resultSet.getDouble("taxAmount"),
                        resultSet.getDouble("finalAmount"),
                        resultSet.getString("paymentMethod"),
                        resultSet.getString("paymentStatus"),
                        resultSet.getTimestamp("createdAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving billing details for booking ID " + bookingId, e);
        }
        return null;
    }

    @Override
    public boolean saveBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException {
        String query = "INSERT INTO Billing (bookingId, totalAmount, taxAmount, discountAmount, finalAmount, paymentMethod, paymentStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            preparedStatement.setDouble(2, totalAmount);
            preparedStatement.setDouble(3, taxAmount);
            preparedStatement.setDouble(4, discountAmount);
            preparedStatement.setDouble(5, finalAmount);
            preparedStatement.setString(6, paymentMethod);
            preparedStatement.setString(7, paymentStatus);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error saving billing information for booking ID " + bookingId, e);
        }
    }

    @Override
    public boolean updateBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException {
        String query = "UPDATE Billing SET totalAmount = ?, taxAmount = ?, discountAmount = ?, finalAmount = ?, paymentMethod = ?, paymentStatus = ? WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, totalAmount);
            preparedStatement.setDouble(2, taxAmount);
            preparedStatement.setDouble(3, discountAmount);
            preparedStatement.setDouble(4, finalAmount);
            preparedStatement.setString(5, paymentMethod);
            preparedStatement.setString(6, paymentStatus);
            preparedStatement.setInt(7, bookingId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating billing information for booking ID " + bookingId, e);
        }
    }

    @Override
    public boolean deleteBill(int bookingId) throws SQLException {
        String query = "DELETE FROM Billing WHERE bookingId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting billing information for booking ID " + bookingId, e);
        }
    }
}
