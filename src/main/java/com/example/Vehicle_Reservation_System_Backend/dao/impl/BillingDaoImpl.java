package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BillingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BillingEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDaoImpl implements BillingDao {

    private Connection connection;

    // Constructor to accept shared Connection instance
    public BillingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public BillingDTO getBillByBookingId(int bookingId) throws SQLException {
        connection = DBConnection.getInstance().getConnection();
        try {
            String query = "SELECT * FROM billing WHERE bookingId = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BillingDTO billing = new BillingDTO();
                return billing;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving billing details for bookingId: " + bookingId);
            throw new SQLException("Error occurred while retrieving billing details.", e);
        }
        return null;
    }

    @Override
    public boolean saveBill(int bookingId, double totalAmount, double taxAmount, double discountAmount, double finalAmount, String paymentMethod, String paymentStatus) throws SQLException {
        connection = DBConnection.getInstance().getConnection();
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
        connection = DBConnection.getInstance().getConnection();
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
        connection = DBConnection.getInstance().getConnection();
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



    @Override
    public List<BillingDTO> getAllBills() throws SQLException {
        String query = "SELECT * FROM Billing";
        connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BillingDTO> billingList = new ArrayList<>();

            while (resultSet.next()) {
                BillingDTO billingDTO = new BillingDTO(
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
                billingList.add(billingDTO);
            }
            return billingList;
        }
    }



    @Override
    public List<BillingEntity> searchPayments(String searchTerm) {
        List<BillingEntity> payments = new ArrayList<>();

        String query = "SELECT * FROM billing WHERE "
                + "paymentMethod LIKE ? OR "
                + "paymentStatus LIKE ? OR "
                + "billId LIKE ? OR "
                + "bookingId LIKE ? OR "
                + "totalAmount LIKE ? OR "
                + "discountAmount LIKE ? OR "
                + "taxAmount LIKE ? OR "
                + "finalAmount LIKE ? OR "
                + "createdAt LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Setting search term to each parameter
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            stmt.setString(4, "%" + searchTerm + "%");
            stmt.setString(5, "%" + searchTerm + "%");
            stmt.setString(6, "%" + searchTerm + "%");
            stmt.setString(7, "%" + searchTerm + "%");
            stmt.setString(8, "%" + searchTerm + "%");
            stmt.setString(9, "%" + searchTerm + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(new BillingEntity(
                        rs.getInt("billId"),
                        rs.getInt("bookingId"),
                        rs.getDouble("totalAmount"),
                        rs.getDouble("discountAmount"),
                        rs.getDouble("taxAmount"),
                        rs.getDouble("finalAmount"),
                        rs.getString("paymentMethod"),
                        rs.getString("paymentStatus"),
                        rs.getTimestamp("createdAt")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

}
