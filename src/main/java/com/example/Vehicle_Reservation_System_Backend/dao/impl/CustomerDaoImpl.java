package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveCustomer(CustomerEntity customerEntity) throws SQLException {
        // Check if the email or NIC already exists
        try {
            String queryCheckExistence = "SELECT COUNT(*) FROM customer WHERE email = ? OR nic = ?";
            PreparedStatement stmt = connection.prepareStatement(queryCheckExistence);
            stmt.setString(1, customerEntity.getEmail());
            stmt.setString(2, customerEntity.getNic());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new AlreadyException("Customer with email or NIC already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // If no existing record found, proceed to insert
        String query = "INSERT INTO customer (userId, name, address, nic, phoneNumber, registrationDate, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerEntity.getUserId());
            stmt.setString(2, customerEntity.getName());
            stmt.setString(3, customerEntity.getAddress());
            stmt.setString(4, customerEntity.getNic());
            stmt.setString(5, customerEntity.getPhoneNumber());
            stmt.setString(6, customerEntity.getRegistrationDate());
            stmt.setString(7, customerEntity.getEmail());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public CustomerEntity getById(int id) {
        String query = "SELECT * FROM customer WHERE customerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CustomerEntity(
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("nic"),
                        rs.getString("phoneNumber"),
                        rs.getString("registrationDate"),
                        rs.getString("email")
                );
            } else {
                throw new NotFoundException("Customer with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existsById(int id) {
        // Check if the customer exists before updating
        connection = DBConnection.getInstance().getConnection();
        String queryCheckExistence = "SELECT COUNT(*) FROM customer WHERE customerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCustomer(CustomerEntity customerEntity) {
        // Check if the customer exists before updating
        if(!existsById(customerEntity.getCustomerId())) throw new NotFoundException("Customer with ID " + customerEntity.getCustomerId() + " not found.");

        // Proceed to update the customer
        String query = "UPDATE customer SET name = ?, address = ?, nic = ?, phoneNumber = ?, registrationDate = ?, email = ? WHERE customerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customerEntity.getName());
            stmt.setString(2, customerEntity.getAddress());
            stmt.setString(3, customerEntity.getNic());
            stmt.setString(4, customerEntity.getPhoneNumber());
            stmt.setString(5, customerEntity.getRegistrationDate());
            stmt.setString(6, customerEntity.getEmail());
            stmt.setInt(7, customerEntity.getCustomerId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        // First, check if the customer exists
        String queryCheckExistence = "SELECT COUNT(*) FROM customer WHERE customerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheckExistence)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new NotFoundException("Customer with ID " + customerId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Now, proceed to delete the customer
        String query = "DELETE FROM customer WHERE customerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CustomerEntity> getAllCustomers() {
        List<CustomerEntity> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customers.add(new CustomerEntity(
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("nic"),
                        rs.getString("phoneNumber"),
                        rs.getString("registrationDate"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public List<CustomerEntity> searchCustomers(String searchTerm) {
        List<CustomerEntity> customers = new ArrayList<>();

        // Modified query to search across multiple fields
        String query = "SELECT * FROM customer WHERE name LIKE ? OR email LIKE ? OR address LIKE ? OR phoneNumber LIKE ? OR nic LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set the search term for all the fields
            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            stmt.setString(4, "%" + searchTerm + "%");
            stmt.setString(5, "%" + searchTerm + "%");

            ResultSet rs = stmt.executeQuery();

            // Loop through the result set and create CustomerEntity objects
            while (rs.next()) {
                customers.add(new CustomerEntity(
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("nic"),
                        rs.getString("phoneNumber"),
                        rs.getString("registrationDate"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

}
