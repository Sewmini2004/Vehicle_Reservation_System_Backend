package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.*;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.*;
import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.utils.BookingConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {
    private BookingDao bookingDao = new BookingDaoImpl();
    private CustomerDao customerDao = new CustomerDaoImpl();
    private DriverDao driverDao = new DriverDaoImpl();
    private VehicleDao vehicleDao = new VehicleDaoImpl();
    private BillingDao billingDao = new BillingDaoImpl();

    @Override
    public boolean addBooking(BookingDTO bookingDTO) throws SQLException {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false); // Start transaction

            if (!customerDao.existsById(bookingDTO.getCustomerId())) {
                throw new SQLException("Customer ID not found.");
            }
            if (!vehicleDao.existsById(bookingDTO.getVehicleId())) {
                throw new SQLException("Vehicle ID not found.");
            }
            if (!driverDao.existsById(bookingDTO.getDriverId())) {
                throw new SQLException("Driver ID not found.");
            }

            // Save booking entity
            BookingEntity entity = new BookingEntity(bookingDTO);
            boolean bookingSaved = bookingDao.saveBooking(entity);

            if (!bookingSaved) {
                throw new SQLException("Failed to save booking.");
            }

            // Billing Calculation
            double totalAmount = bookingDTO.getTotalBill();
            double taxAmount = totalAmount * 0.10;
            double discountAmount = totalAmount * 0.05;
            double finalAmount = totalAmount + taxAmount - discountAmount;

            boolean billSaved = billingDao.saveBill(entity.getBookingId(), totalAmount, taxAmount, discountAmount, finalAmount, "Credit Card", "Pending");

            if (!billSaved) {
                throw new SQLException("Failed to save bill.");
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
            System.err.println("Error in addBooking: " + ex.getMessage());

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
    public BookingDTO getBookingById(int bookingId) {
        BookingEntity entity = bookingDao.getBookingById(bookingId);
        if (entity == null) {
            throw new NotFoundException("Booking with ID " + bookingId + " not found.");
        }

        BookingDTO bookingDTO = BookingConverter.convertToDTO(entity);

        // Fetch billing details and attach to booking
        BillingDTO billingDTO = billingDao.getBillByBookingId(bookingId);
        if (billingDTO != null) {
            bookingDTO.setBillingDetails(billingDTO);
        }

        return bookingDTO;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingEntity> bookingEntities = bookingDao.getAllBookings();
        List<BookingDTO> bookingDTOs = bookingEntities.stream()
                .map(BookingConverter::convertToDTO)
                .collect(Collectors.toList());

        for (BookingDTO bookingDTO : bookingDTOs) {
            BillingDTO billingDTO = billingDao.getBillByBookingId(bookingDTO.getBookingId());
            if (billingDTO != null) {
                bookingDTO.setBillingDetails(billingDTO);
            }
        }

        return bookingDTOs;
    }

    @Override
    public boolean updateBooking(BookingDTO bookingDTO) throws SQLException {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            BookingEntity entity = new BookingEntity(bookingDTO);
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
        connection.setAutoCommit(false);

        try {
            billingDao.deleteBill(bookingId);
            boolean deleted = bookingDao.deleteBooking(bookingId);

            if (!deleted) {
                throw new SQLException("Failed to delete booking.");
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}
