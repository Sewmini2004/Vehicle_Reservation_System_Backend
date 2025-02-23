package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BookingDao;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;
import com.example.Vehicle_Reservation_System_Backend.utils.DateFormatUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoImpl implements BookingDao {
    private Connection connection;

    public BookingDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int saveBooking(BookingEntity booking) {
        String query = "INSERT INTO booking (customerId, vehicleId, driverID, pickupLocation, dropLocation, bookingDate, carType, totalBill) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getVehicleId());
            stmt.setInt(3, booking.getDriverId());
            stmt.setString(4, booking.getPickupLocation());
            stmt.setString(5, booking.getDropLocation());
            stmt.setDate(6, new java.sql.Date(booking.getBookingDate().getTime()));
            stmt.setString(7, booking.getCarType());
            stmt.setDouble(8, booking.getTotalBill());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Retrieve the generated bookingId
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public BookingEntity getBookingById(int bookingId) {
        String query = "SELECT * FROM booking WHERE bookingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BookingEntity(
                        rs.getInt("bookingId"),
                        rs.getInt("customerId"),
                        rs.getInt("vehicleId"),
                        rs.getInt("driverID"),
                        rs.getString("pickupLocation"),
                        rs.getString("dropLocation"),
                        rs.getDate("bookingDate"),
                        rs.getString("carType"),
                        rs.getDouble("totalBill")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BookingEntity> getAllBookings() {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                bookings.add(new BookingEntity(
                        rs.getInt("bookingId"),
                        rs.getInt("customerId"),
                        rs.getInt("vehicleId"),
                        rs.getInt("driverID"),
                        rs.getString("pickupLocation"),
                        rs.getString("dropLocation"),
                        rs.getDate("bookingDate"),
                        rs.getString("carType"),
                        rs.getDouble("totalBill")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(BookingEntity booking) {
        String query = "UPDATE booking SET pickupLocation = ?, dropLocation = ?, carType = ?, totalBill = ? , bookingDate = ? WHERE bookingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, booking.getPickupLocation());
            stmt.setString(2, booking.getDropLocation());
            stmt.setString(3, booking.getCarType());
            stmt.setDouble(4, booking.getTotalBill());
            stmt.setDate(5, DateFormatUtils.convertUtilToSqlDate(booking.getBookingDate()));
            stmt.setInt(6, booking.getBookingId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM booking WHERE bookingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
