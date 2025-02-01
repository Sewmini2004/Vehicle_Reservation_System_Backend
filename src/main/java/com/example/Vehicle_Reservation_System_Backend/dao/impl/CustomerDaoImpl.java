package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private Connection connection;

    public CustomerDaoImpl() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public boolean saveCustomer(CustomerEntity customerEntity) {
        try {
            String query = "INSERT INTO customers (user_id, name, address, nic, phone_number, registration_date, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, customerEntity.getUserId());
            stmt.setString(2, customerEntity.getName());
            stmt.setString(3, customerEntity.getAddress());
            stmt.setString(4, customerEntity.getNic());
            stmt.setString(5, customerEntity.getPhoneNumber());
            stmt.setString(6, customerEntity.getRegistrationDate());
            stmt.setString(7, customerEntity.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCustomer(CustomerEntity customer) {
        try {
            String query = "UPDATE customers SET name = ?, address = ?, nic = ?, phone_number = ?, registration_date = ?, email = ? WHERE customer_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getNic());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getRegistrationDate());
            stmt.setString(6, customer.getEmail());
            stmt.setInt(7, customer.getCustomerId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(int id) {
        try {
            String query = "DELETE FROM customers WHERE customer_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CustomerEntity> getAllCustomers() {
        List<CustomerEntity> customers = new ArrayList<>();
        try {
            String query = "SELECT * FROM customers";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               CustomerEntity customer = new CustomerEntity(
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("nic"),
                        rs.getString("phone_number"),
                        rs.getString("registration_date"),
                        rs.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public CustomerEntity getById(int id) {
        CustomerEntity customer = null;
        try {
            String query = "SELECT * FROM customers WHERE customer_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customer = new CustomerEntity(
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("nic"),
                        rs.getString("phone_number"),
                        rs.getString("registration_date"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
