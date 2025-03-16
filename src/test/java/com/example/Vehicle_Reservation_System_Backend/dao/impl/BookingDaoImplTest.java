package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;
import com.example.Vehicle_Reservation_System_Backend.utils.DateFormatUtils;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingDaoImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStmt;

    @Mock
    private ResultSet mockResultSet;

    private BookingDaoImpl bookingDaoImpl;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        bookingDaoImpl = new BookingDaoImpl(mockConnection);
    }

    @AfterEach
    void tearDown() {
        // Close mocks or perform necessary cleanup if needed
    }

    @Test
    void testSaveBooking_Success() throws SQLException {
        // Create a mock BookingEntity
        BookingEntity booking = new BookingEntity.Builder()
                .bookingId(1)
                .customerId(1)
                .vehicleId(1)
                .driverId(1)
                .pickupLocation("Location A")
                .dropLocation("Location B")
                .bookingDate(new java.util.Date())  // Current date
                .carType("Sedan")
                .totalBill(100.0)
                .distance(2)
                .cancelStatus("")  // Empty cancel status
                .build();

        // Mock the PreparedStatement behavior for the insert query
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        // Test the saveBooking method
        int bookingId = bookingDaoImpl.saveBooking(booking);
        assertEquals(1, bookingId); // Assert that the generated ID is correct

        // Verify the expected methods were called
        verify(mockStmt, times(-1)).executeUpdate();
        verify(mockStmt, times(-1)).getGeneratedKeys();
    }

    @Test
    void testSaveBooking_Failure() throws SQLException {
        // Create a mock BookingEntity
        BookingEntity booking = new BookingEntity.Builder()
                .bookingId(1)
                .customerId(1)
                .vehicleId(1)
                .driverId(1)
                .pickupLocation("Location A")
                .dropLocation("Location B")
                .bookingDate(new java.util.Date()) // Use the current date
                .carType("Sedan")
                .totalBill(100.0)
                .distance(4) // Distance in km (or whatever unit you need)
                .cancelStatus("") // Empty string for cancel status
                .build();


        // Mock the PreparedStatement behavior to simulate no generated keys
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate that one row is affected
        when(mockStmt.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate that no keys are returned

        // Test the saveBooking method and assert failure case
        int bookingId = bookingDaoImpl.saveBooking(booking);
        assertEquals(-1, bookingId); // Assert that -1 is returned due to no ID being generated

        // Verify the expected methods were called
        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).getGeneratedKeys();
    }


    @Test
    void testGetBookingById_Success() throws SQLException {
        // Mock the behavior for the getBookingById method
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("bookingId")).thenReturn(1);
        when(mockResultSet.getInt("customerId")).thenReturn(1);
        when(mockResultSet.getInt("vehicleId")).thenReturn(1);
        when(mockResultSet.getInt("driverID")).thenReturn(1);
        when(mockResultSet.getString("pickupLocation")).thenReturn("Location A");
        when(mockResultSet.getString("dropLocation")).thenReturn("Location B");
        when(mockResultSet.getDate("bookingDate")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockResultSet.getString("carType")).thenReturn("Sedan");
        when(mockResultSet.getDouble("totalBill")).thenReturn(100.0);

        // Simulate the result of getBookingById
        BookingEntity result = bookingDaoImpl.getBookingById(1);
        assertNotNull(result);
        assertEquals(1, result.getBookingId()); // Check if the returned booking ID matches

        // Verify the query was executed
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testGetBookingById_Failure() throws SQLException {
        // Mock the behavior to simulate no booking found
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no results

        // Test and verify the result
        BookingEntity result = bookingDaoImpl.getBookingById(999); // Non-existing ID
        assertNull(result); // Assert that the result is null

        // Verify the query was executed
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testUpdateBooking_Success() throws SQLException {
        BookingEntity booking = new BookingEntity.Builder()
                .bookingId(1)
                .customerId(1)
                .vehicleId(1)
                .driverId(1)
                .pickupLocation("Location A")
                .dropLocation("Location B")
                .bookingDate(new java.util.Date())
                .carType("Sedan")
                .totalBill(100.0)
                .distance(2)
                .cancelStatus("")
                .build();

        // Mock PreparedStatement for the update query
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate successful update

        // Test the updateBooking method
        boolean result = bookingDaoImpl.updateBooking(booking);
        assertTrue(result); // Assert that the update was successful

        // Verify that executeUpdate was called
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testDeleteBooking_Success() throws SQLException {
        // Mock PreparedStatement for the delete query
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate successful deletion

        // Test the deleteBooking method
        boolean result = bookingDaoImpl.deleteBooking(1);
        assertTrue(result); // Assert that the deletion was successful

        // Verify the delete query was executed
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testDeleteBooking_Failure() throws SQLException {
        // Mock PreparedStatement for the delete query
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0); // Simulate no rows deleted

        // Test the deleteBooking method
        boolean result = bookingDaoImpl.deleteBooking(999);
        assertFalse(result); // Assert that the deletion failed

        // Verify the delete query was executed
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testGetAllBookings() throws SQLException {
        // Mock the behavior for the getAllBookings method
        when(mockConnection.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Simulate one result

        // Mock data returned from the result set
        when(mockResultSet.getInt("bookingId")).thenReturn(1);
        when(mockResultSet.getInt("customerId")).thenReturn(1);
        when(mockResultSet.getInt("vehicleId")).thenReturn(1);
        when(mockResultSet.getInt("driverID")).thenReturn(1);
        when(mockResultSet.getString("pickupLocation")).thenReturn("Location A");
        when(mockResultSet.getString("dropLocation")).thenReturn("Location B");
        when(mockResultSet.getDate("bookingDate")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockResultSet.getString("carType")).thenReturn("Sedan");
        when(mockResultSet.getDouble("totalBill")).thenReturn(100.0);

        // Test getAllBookings
        List<BookingEntity> bookings = bookingDaoImpl.getAllBookings();
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty()); // Assert that the list is not empty

        // Verify that the query was executed
        verify(mockStmt, times(1)).executeQuery(anyString());
    }
}
