package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DriverDaoImplTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStmt;

    @Mock
    private ResultSet mockResultSet;

    private DriverDaoImpl driverDaoImpl;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        driverDaoImpl = new DriverDaoImpl(mockConnection);
    }

    @Test
    void testSaveDriver_WhenDriverAlreadyExists() throws SQLException {
        DriverEntity driverEntity = new DriverEntity.Builder()
                .driverId(1)
                .name("John Doe")
                .licenseNumber("AB1234")
                .status("Active")
                .shiftTiming("Morning")
                .salary(50000)
                .experienceYears(5)
                .phoneNumber("123456789")
                .build();

        // Mock the PreparedStatement to simulate the existence check
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate that a driver with the same license or phone exists
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulating a match

        // Test the behavior when the driver already exists
        assertThrows(AlreadyException.class, () -> driverDaoImpl.saveDriver(driverEntity));

        // Verify that the right query is being executed
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testSaveDriver_Success() throws SQLException {
        DriverEntity driverEntity = new DriverEntity.Builder()
                .driverId(1)
                .name("John Doe")
                .licenseNumber("AB1234")
                .status("Active")
                .shiftTiming("Morning")
                .salary(50000)
                .experienceYears(5)
                .phoneNumber("123456789")
                .build();

        // Mock the PreparedStatement and the ResultSet
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);

        // Mock the ResultSet to return a valid result
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no driver already exists with the same license or phone number

        // Mock the execution of the insertion query
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate successful insertion

        boolean result = driverDaoImpl.saveDriver(driverEntity);
        assertTrue(result);

        // Verify that the query was executed and insertion happened
        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testGetById_WhenDriverExists() throws SQLException {
        DriverEntity expectedDriver = new DriverEntity.Builder()
                .driverId(1)
                .name("John Doe")
                .licenseNumber("AB1234")
                .status("Active")
                .shiftTiming("Morning")
                .salary(50000)
                .experienceYears(5)
                .phoneNumber("123456789")
                .build();

        // Mock the PreparedStatement and ResultSet to simulate a driver being found
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);  // Simulate that the driver exists
        when(mockResultSet.getInt("driverId")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("John Doe");
        when(mockResultSet.getString("licenseNumber")).thenReturn("AB1234");
        when(mockResultSet.getString("status")).thenReturn("Active");
        when(mockResultSet.getString("shiftTiming")).thenReturn("Morning");
        when(mockResultSet.getDouble("salary")).thenReturn(50000.0);
        when(mockResultSet.getInt("experienceYears")).thenReturn(5);
        when(mockResultSet.getString("phoneNumber")).thenReturn("123456789");

        DriverEntity result = driverDaoImpl.getById(1);
        assertNotNull(result);
        assertEquals(expectedDriver.getDriverId(), result.getDriverId());
        assertEquals(expectedDriver.getName(), result.getName());
    }

    @Test
    void testGetById_WhenDriverDoesNotExist() throws SQLException {
        // Mock the PreparedStatement and ResultSet to simulate no driver found
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);  // Simulate no driver found

        assertThrows(NotFoundException.class, () -> driverDaoImpl.getById(999));
    }

    @Test
    void testUpdateDriver_Success() throws SQLException {
        DriverEntity driverEntity = new DriverEntity.Builder()
                .driverId(1)
                .name("John Doe")
                .licenseNumber("AB1234")
                .status("Active")
                .shiftTiming("Morning")
                .salary(50000)
                .experienceYears(5)
                .phoneNumber("123456789")
                .build();

        // Mock the PreparedStatement for checking if the driver exists
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);  // Simulate that the driver exists
        when(mockResultSet.getInt(1)).thenReturn(1);  // Simulate that a valid result is returned

        // Mock the PreparedStatement for executing the update query
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate successful update

        boolean result = driverDaoImpl.updateDriver(driverEntity);
        assertTrue(result);

        // Verify that the update query is executed
        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testDeleteDriver_WhenDriverExists() throws SQLException {
        // Mock the PreparedStatement and ResultSet for checking driver existence
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate that the driver exists
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a valid result

        // Mock the PreparedStatement for deleting the driver
        when(mockStmt.executeUpdate()).thenReturn(1); // Simulate successful deletion

        boolean result = driverDaoImpl.deleteDriver(1);
        assertTrue(result);

        // Verify that the delete query is executed
        verify(mockStmt, times(1)).executeUpdate();
        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testDeleteDriver_WhenDriverDoesNotExist() throws SQLException {
        // Mock the PreparedStatement for checking driver existence
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate that the driver does not exist

        boolean result = driverDaoImpl.deleteDriver(999);
        assertFalse(result);

        verify(mockStmt, times(1)).executeQuery();
    }

    @Test
    void testExistsById_WhenDriverExists() throws SQLException {
        // Mock the PreparedStatement and ResultSet for existence check
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate driver exists
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a valid result

        boolean result = driverDaoImpl.existsById(1);
        assertTrue(result);
    }

    @Test
    void testExistsById_WhenDriverDoesNotExist() throws SQLException {
        // Mock the PreparedStatement and ResultSet for existence check
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate driver does not exist

        boolean result = driverDaoImpl.existsById(999);
        assertFalse(result);
    }
}
