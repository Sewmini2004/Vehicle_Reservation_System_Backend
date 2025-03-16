package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.*;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.utils.BookingConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private BookingDao bookingDao;
    private CustomerDao customerDao;
    private DriverDao driverDao;
    private VehicleDao vehicleDao;
    private BillingDao billingDao;

    public BookingServiceImpl(BookingDao bookingDao, CustomerDao customerDao, DriverDao driverDao, VehicleDao vehicleDao, BillingDao billingDao) {
        this.bookingDao = bookingDao;
        this.customerDao = customerDao;
        this.driverDao = driverDao;
        this.vehicleDao = vehicleDao;
        this.billingDao = billingDao;
    }

    @Override
    public boolean addBooking(BookingDTO bookingDTO) throws SQLException {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Validate if the customer, vehicle, and driver exist
            if (!customerDao.existsById(bookingDTO.getCustomerId())) {
                throw new SQLException("Customer ID not found.");
            }
            if (!vehicleDao.existsById(bookingDTO.getVehicleId())) {
                throw new SQLException("Vehicle ID not found.");
            }
            if (!driverDao.existsById(bookingDTO.getDriverId())) {
                throw new SQLException("Driver ID not found.");
            }

            // Convert DTO to Entity using the Builder pattern
            BookingEntity entity = new BookingEntity.Builder()
                    .bookingId(0)
                    .customerId(bookingDTO.getCustomerId())
                    .vehicleId(bookingDTO.getVehicleId())
                    .driverId(bookingDTO.getDriverId())
                    .pickupLocation(bookingDTO.getPickupLocation())
                    .dropLocation(bookingDTO.getDropLocation())
                    .bookingDate(bookingDTO.getBookingDate())
                    .carType(bookingDTO.getCarType())
                    .totalBill(bookingDTO.getTotalBill())
                    .cancelStatus(bookingDTO.getCancelStatus())
                    .distance(bookingDTO.getDistance())
                    .customerName(bookingDTO.getCustomerName())
                    .driverName(bookingDTO.getDriverName())
                    .vehicleModel(bookingDTO.getVehicleModel())
                    .vehicleRegistrationNumber(bookingDTO.getVehicleRegistrationNumber())
                    .build();

            int savedBookingId = bookingDao.saveBooking(entity);
            entity.setBookingId(savedBookingId);
            System.out.println("LOGG::Saved Booking ID::" + savedBookingId);

            if (savedBookingId < 0) {
                throw new SQLException("Failed to save booking.");
            }

            // Billing Calculation
            double totalAmount = bookingDTO.getTotalBill();
            double taxAmount = totalAmount * 0.10;
            double discountAmount = totalAmount * 0.05;
            double finalAmount = totalAmount + taxAmount - discountAmount;

            boolean billSaved = billingDao.saveBill(entity.getBookingId(), totalAmount, taxAmount, discountAmount, finalAmount, bookingDTO.getBillingDetails().getPaymentMethod(), bookingDTO.getBillingDetails().getPaymentStatus());

            if (!billSaved) {
                throw new SQLException("Failed to save bill.");
            }

            connection.commit();
            success = true;

        } catch (SQLException ex) {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Error in addBooking: " + ex.getMessage());

        } finally {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        return success;
    }

    @Override
    public BookingDTO getBookingById(int bookingId) throws SQLException {
        BookingEntity bookingEntity = bookingDao.getBookingById(bookingId);

        if (bookingEntity != null) {
            return BookingConverter.convertToDTO(bookingEntity);
        }
        return null;
    }

    @Override
    public List<BookingDTO> getAllBookings() throws SQLException {
        List<BookingEntity> bookingEntities = bookingDao.getAllBookings();
        List<BookingDTO> bookingDTOs = bookingEntities.stream()
                .map(BookingConverter::convertToDTO)
                .collect(Collectors.toList());

        // Fetch billing details and attach to each booking DTO
        for (BookingDTO bookingDTO : bookingDTOs) {
            BillingDTO billingDTO = billingDao.getBillByBookingId(bookingDTO.getBookingId());
            if (billingDTO != null) {
                bookingDTO.setBillingDetails(billingDTO);
            }
        }

        return bookingDTOs;
    }

    @Override
    public boolean updateBooking(BookingDTO bookingDTO) {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            // Convert DTO to Entity and update booking using the Builder
            BookingEntity entity = new BookingEntity.Builder()
                    .bookingId(bookingDTO.getBookingId()) // Ensure the booking ID is carried over
                    .customerId(bookingDTO.getCustomerId())
                    .vehicleId(bookingDTO.getVehicleId())
                    .driverId(bookingDTO.getDriverId())
                    .pickupLocation(bookingDTO.getPickupLocation())
                    .dropLocation(bookingDTO.getDropLocation())
                    .bookingDate(bookingDTO.getBookingDate())
                    .carType(bookingDTO.getCarType())
                    .totalBill(bookingDTO.getTotalBill())
                    .cancelStatus(bookingDTO.getCancelStatus())
                    .distance(bookingDTO.getDistance())
                    .customerName(bookingDTO.getCustomerName())
                    .driverName(bookingDTO.getDriverName())
                    .vehicleModel(bookingDTO.getVehicleModel())
                    .vehicleRegistrationNumber(bookingDTO.getVehicleRegistrationNumber())
                    .build();

            boolean bookingUpdated = bookingDao.updateBooking(entity);

            if (!bookingUpdated) {
                throw new SQLException("Failed to update booking.");
            }

            // Recalculate billing details
            double totalAmount = bookingDTO.getTotalBill();
            double taxAmount = totalAmount * 0.10;
            double discountAmount = totalAmount * 0.05;
            double finalAmount = totalAmount + taxAmount - discountAmount;

            boolean billUpdated = billingDao.updateBill(entity.getBookingId(), totalAmount, taxAmount, discountAmount, finalAmount, "Credit Card", "Completed");

            if (!billUpdated) {
                throw new SQLException("Failed to update bill.");
            }

            connection.commit();
            success = true;

        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Error in updateBooking: " + ex.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }

        return success;
    }

    @Override
    public boolean deleteBooking(int bookingId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false); // Start transaction

        try {
            // Check if the booking exists before trying to delete it
            BookingEntity bookingEntity = bookingDao.getBookingById(bookingId);
            if (bookingEntity == null) {
                throw new NotFoundException("Booking with ID " + bookingId + " not found.");
            }

            // Delete associated bill before deleting the booking
            boolean billDeleted = billingDao.deleteBill(bookingId);
            if (!billDeleted) {
                throw new SQLException("Failed to delete billing information for booking ID " + bookingId);
            }

            // Delete the booking itself
            boolean bookingDeleted = bookingDao.deleteBooking(bookingId);
            if (!bookingDeleted) {
                throw new SQLException("Failed to delete booking.");
            }

            // Commit transaction if both operations were successful
            connection.commit();
            return true;

        } catch (SQLException e) {
            // Rollback transaction if any error occurs
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;  // Propagate the exception to be handled by the calling method

        } finally {
            // Always reset autoCommit to true and close the connection
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset autoCommit to default
                    connection.close(); // Ensure the connection is closed
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<BookingDTO> searchBookings(String searchTerm) {
        List<BookingEntity> bookingEntities = bookingDao.searchBookings(searchTerm);
        return bookingEntities.stream()
                .map(BookingConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
