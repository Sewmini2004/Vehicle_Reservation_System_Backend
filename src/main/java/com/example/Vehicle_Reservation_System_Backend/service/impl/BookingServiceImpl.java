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
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.stream.Collectors;

    public class BookingServiceImpl implements BookingService {
        private BookingDao bookingDao;
        private CustomerDao customerDao;
        private DriverDao driverDao;
        private VehicleDao vehicleDao;
        private BillingDao billingDao;

        public BookingServiceImpl(BookingDao bookingDao,CustomerDao customerDao,DriverDao driverDao,VehicleDao vehicleDao,BillingDao billingDao) {
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
                    throw new SQLException  ("Driver ID not found.");
                }

                // Convert DTO to Entity and save booking
                BookingEntity entity = BookingConverter.convertToEntity(bookingDTO);
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
                    .map(BookingConverter::convertToDTO) // Use the BookingConverter to map each entity to a DTO
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

                // Convert DTO to Entity and update booking
                BookingEntity entity = BookingConverter.convertToEntity(bookingDTO);
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


        public List<BookingDTO> searchBookings(String searchTerm) throws SQLException {
            List<BookingDTO> result = new ArrayList<>();
            Connection connection = DBConnection.getInstance().getConnection();

            // SQL query to search across multiple columns including the bookingDate
            String query = "SELECT bo.*, ca.name as customerName, ve.model as vehicleModel, ve.registrationNumber as vehicleRegistrationNumber, " +
                    "dr.name as driverName FROM booking bo " +
                    "LEFT JOIN customer ca ON bo.customerId = ca.customerId " +
                    "LEFT JOIN vehicle ve ON bo.vehicleId = ve.vehicleId " +
                    "LEFT JOIN driver dr ON bo.driverId = dr.driverId " +
                    "WHERE ca.name LIKE ? OR ve.model LIKE ? OR bo.pickupLocation LIKE ? OR bo.dropLocation LIKE ? OR dr.name LIKE ? OR bo.bookingDate LIKE ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                String likeSearchTerm = "%" + searchTerm + "%";
                stmt.setString(1, likeSearchTerm);
                stmt.setString(2, likeSearchTerm);
                stmt.setString(3, likeSearchTerm);
                stmt.setString(4, likeSearchTerm);
                stmt.setString(5, likeSearchTerm);

                if (searchTerm.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    stmt.setString(6, searchTerm);
                } else {
                    stmt.setString(6, "%");
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    result.add(new BookingDTO(
                            rs.getInt("bookingId"),
                            rs.getInt("customerId"),
                            rs.getInt("vehicleId"),
                            rs.getInt("driverID"),
                            rs.getString("pickupLocation"),
                            rs.getString("dropLocation"),
                            rs.getDate("bookingDate"),
                            rs.getString("carType"),
                            rs.getDouble("totalBill"),
                            rs.getString("cancelStatus"),
                            rs.getDouble("distance"),
                            rs.getString("customerName"),
                            rs.getString("driverName"),
                            rs.getString("vehicleModel"),
                            rs.getString("vehicleRegistrationNumber")
                    ));
                }
            }

            return result;
        }


    }
