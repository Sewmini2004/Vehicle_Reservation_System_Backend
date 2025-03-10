package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.RegisterDao;
import com.example.Vehicle_Reservation_System_Backend.entity.RegisterEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterDaoImpl implements RegisterDao {

    private Connection connection;

    public RegisterDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveUser(RegisterEntity registerEntity) throws SQLException {
        // Check if the username already exists
        String queryCheck = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(queryCheck)) {
            stmt.setString(1, registerEntity.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new AlreadyException("Username already exists.");
            }
        }

        // Insert new user
        String query = "INSERT INTO user (userId, username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, registerEntity.getUserId());
            stmt.setString(2, registerEntity.getUsername());
            stmt.setString(3, registerEntity.getPassword());
            stmt.setString(4, registerEntity.getFirstName());
            stmt.setString(5, registerEntity.getLastName());
            stmt.setString(6, registerEntity.getEmail());
            stmt.executeUpdate();
            return true;
        }
    }

    @Override
    public RegisterEntity getById(int userId) {
        String query = "SELECT * FROM user WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RegisterEntity(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email")
                );
            } else {
                throw new NotFoundException("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUser(RegisterEntity registerEntity) {
        String query = "UPDATE user SET password = ?, firstName = ?, lastName = ?, email = ? WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, registerEntity.getPassword());
            stmt.setString(2, registerEntity.getFirstName());
            stmt.setString(3, registerEntity.getLastName());
            stmt.setString(4, registerEntity.getEmail());
            stmt.setInt(5, registerEntity.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM user WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<RegisterEntity> getAllUsers() {
        List<RegisterEntity> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.add(new RegisterEntity(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public RegisterEntity getByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RegisterEntity(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
