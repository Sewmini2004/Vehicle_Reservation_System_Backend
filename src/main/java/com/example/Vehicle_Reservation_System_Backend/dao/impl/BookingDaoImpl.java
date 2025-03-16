package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BookingDao;
import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
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
        connection = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO booking (customerId, vehicleId, driverID, pickupLocation, dropLocation, bookingDate, carType, totalBill,cancelStatus,distance) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getVehicleId());
            stmt.setInt(3, booking.getDriverId());
            stmt.setString(4, booking.getPickupLocation());
            stmt.setString(5, booking.getDropLocation());
            stmt.setDate(6, new java.sql.Date(booking.getBookingDate().getTime()));
            stmt.setString(7, booking.getCarType());
            stmt.setDouble(8, booking.getTotalBill());
            stmt.setString(9, booking.getCancelStatus());
            stmt.setDouble(10, booking.getDistance());

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
        connection = DBConnection.getInstance().getConnection();
        String query = "SELECT bo.*, ca.name as customerName, ve.model as vehicleModel, ve.registrationNumber " +
                "as vehicleRegistrationNumber, dr.name as driverName " +
                "FROM booking bo " +
                "LEFT JOIN customer ca ON bo.customerId = ca.customerId " +
                "LEFT JOIN vehicle ve ON bo.vehicleId = ve.vehicleId " +
                "LEFT JOIN driver dr ON bo.driverId = dr.driverId " +
                "WHERE bo.bookingId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Use Builder pattern to construct BookingEntity
                return new BookingEntity.Builder()
                        .bookingId(rs.getInt("bookingId"))
                        .customerId(rs.getInt("customerId"))
                        .vehicleId(rs.getInt("vehicleId"))
                        .driverId(rs.getInt("driverID"))
                        .pickupLocation(rs.getString("pickupLocation"))
                        .dropLocation(rs.getString("dropLocation"))
                        .bookingDate(rs.getDate("bookingDate"))
                        .carType(rs.getString("carType"))
                        .totalBill(rs.getDouble("totalBill"))
                        .distance(rs.getDouble("distance"))
                        .cancelStatus(rs.getString("cancelStatus"))
                        .customerName(rs.getString("customerName"))
                        .driverName(rs.getString("driverName"))
                        .vehicleModel(rs.getString("vehicleModel"))
                        .vehicleRegistrationNumber(rs.getString("vehicleRegistrationNumber"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BookingEntity> getAllBookings() {
        connection = DBConnection.getInstance().getConnection();
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT bo.*, ca.name as customerName, ve.model as vehicleModel, ve.registrationNumber as vehicleRegistrationNumber, " +
                "dr.name as driverName, bo.distance FROM booking bo " +
                "LEFT JOIN customer ca ON bo.customerId = ca.customerId " +
                "LEFT JOIN vehicle ve ON bo.vehicleId = ve.vehicleId " +
                "LEFT JOIN driver dr ON bo.driverId = dr.driverId";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Use Builder pattern to create BookingEntity objects
                BookingEntity bookingEntity = new BookingEntity.Builder()
                        .bookingId(rs.getInt("bookingId"))
                        .customerId(rs.getInt("customerId"))
                        .vehicleId(rs.getInt("vehicleId"))
                        .driverId(rs.getInt("driverID"))
                        .pickupLocation(rs.getString("pickupLocation"))
                        .dropLocation(rs.getString("dropLocation"))
                        .bookingDate(rs.getDate("bookingDate"))
                        .carType(rs.getString("carType"))
                        .totalBill(rs.getDouble("totalBill"))
                        .distance(rs.getDouble("distance"))
                        .cancelStatus(rs.getString("cancelStatus"))
                        .customerName(rs.getString("customerName"))
                        .driverName(rs.getString("driverName"))
                        .vehicleModel(rs.getString("vehicleModel"))
                        .vehicleRegistrationNumber(rs.getString("vehicleRegistrationNumber"))
                        .build();

                // Add the created booking entity to the list
                bookings.add(bookingEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(BookingEntity booking) {
        connection = DBConnection.getInstance().getConnection();
        String query = "UPDATE booking SET pickupLocation = ?, dropLocation = ?, carType = ?, totalBill = ? , cancelStatus =?, distance=?  bookingDate = ? WHERE bookingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, booking.getPickupLocation());
            stmt.setString(2, booking.getDropLocation());
            stmt.setString(3, booking.getCarType());
            stmt.setDouble(4, booking.getTotalBill());
            stmt.setString(5, booking.getCancelStatus());
            stmt.setDouble(6, booking.getDistance());
            stmt.setDate(7, DateFormatUtils.convertUtilToSqlDate(booking.getBookingDate()));
            stmt.setInt(8, booking.getBookingId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        connection = DBConnection.getInstance().getConnection();
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


    // Search for bookings based on search term
    public List<BookingEntity> searchBookings(String searchTerm) {
        List<BookingEntity> result = new ArrayList<>();

        // Establish a database connection
        try (Connection connection = DBConnection.getInstance().getConnection()) {

            // SQL query to search across multiple columns
            String query = "SELECT bo.*, ca.name as customerName, ve.model as vehicleModel, ve.registrationNumber as vehicleRegistrationNumber, " +
                    "dr.name as driverName FROM booking bo " +
                    "LEFT JOIN customer ca ON bo.customerId = ca.customerId " +
                    "LEFT JOIN vehicle ve ON bo.vehicleId = ve.vehicleId " +
                    "LEFT JOIN driver dr ON bo.driverId = dr.driverId " +
                    "WHERE ca.name LIKE ? OR ve.model LIKE ? OR bo.pickupLocation LIKE ? OR bo.dropLocation LIKE ? OR dr.name LIKE ? OR bo.bookingDate LIKE ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                String likeSearchTerm = "%" + searchTerm + "%";

                // Set the parameters for the LIKE searches
                stmt.setString(1, likeSearchTerm);
                stmt.setString(2, likeSearchTerm);
                stmt.setString(3, likeSearchTerm);
                stmt.setString(4, likeSearchTerm);
                stmt.setString(5, likeSearchTerm);

                // If search term is a valid date, use it for the bookingDate filter
                if (searchTerm.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    stmt.setString(6, searchTerm);
                } else {
                    stmt.setString(6, "%");
                }

                // Execute the query and process the result
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Use Builder pattern to create the BookingDTO from ResultSet
                        BookingEntity bookingDTO = new BookingEntity.Builder()
                                .bookingId(rs.getInt("bookingId"))
                                .customerId(rs.getInt("customerId"))
                                .vehicleId(rs.getInt("vehicleId"))
                                .driverId(rs.getInt("driverID"))
                                .pickupLocation(rs.getString("pickupLocation"))
                                .dropLocation(rs.getString("dropLocation"))
                                .bookingDate(rs.getDate("bookingDate"))
                                .carType(rs.getString("carType"))
                                .totalBill(rs.getDouble("totalBill"))
                                .cancelStatus(rs.getString("cancelStatus"))
                                .distance(rs.getDouble("distance"))
                                .customerName(rs.getString("customerName"))
                                .driverName(rs.getString("driverName"))
                                .vehicleModel(rs.getString("vehicleModel"))
                                .vehicleRegistrationNumber(rs.getString("vehicleRegistrationNumber"))
                                .build();

                        // Add the bookingDTO to the result list
                        result.add(bookingDTO);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Properly handle exceptions
            throw new RuntimeException("Error while searching for bookings: " + e.getMessage(), e);
        }

        return result;
    }

}
