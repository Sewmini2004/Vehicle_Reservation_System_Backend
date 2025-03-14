package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VehicleDaoImplTest {

    private Connection mockConnection;
    private VehicleDaoImpl vehicleDaoImpl;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);  // Mock the Connection object
        vehicleDaoImpl = new VehicleDaoImpl(mockConnection);  // Create an instance of VehicleDaoImpl with the mock connection
    }

    @AfterEach
    void tearDown() {
        mockConnection = null;
        vehicleDaoImpl = null;
    }

    @Test
    void testSaveVehicle() throws SQLException {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "Sedan", "Model X", "Available", "ABC123", "Petrol", "Car Model X", 4);

        // Mock the PreparedStatement and its execution
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("INSERT INTO vehicle (carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity) VALUES (?, ?, ?, ?, ?, ?, ?)")).thenReturn(mockStmt);

        // Simulate successful execution
        when(mockStmt.executeUpdate()).thenReturn(1);  // Simulate a successful insertion

        // Call the saveVehicle method and assert it returns true
        boolean result = vehicleDaoImpl.saveVehicle(vehicleEntity);
        assertTrue(result);

        // Verify the correct methods were called
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testGetById_WhenVehicleExists() throws SQLException {
        // Create the mock result set with the expected vehicle data
        ResultSet mockResultSet = mock(ResultSet.class);

        // Simulate that the result set has a row of data
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("vehicleId")).thenReturn(1);
        when(mockResultSet.getString("carType")).thenReturn("Sedan");
        when(mockResultSet.getString("model")).thenReturn("Model X");
        when(mockResultSet.getString("availabilityStatus")).thenReturn("Available");
        when(mockResultSet.getString("registrationNumber")).thenReturn("ABC123");
        when(mockResultSet.getString("fuelType")).thenReturn("Petrol");
        when(mockResultSet.getString("carModel")).thenReturn("Car Model X");
        when(mockResultSet.getInt("seatingCapacity")).thenReturn(4);

        // Create the mock PreparedStatement to return the mock result set
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SELECT * FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        // Call getById with an ID and assert it returns the expected VehicleEntity
        VehicleEntity result = vehicleDaoImpl.getById(1);

        // Assert that the result is not null and the values are correct
        assertNotNull(result);
        assertEquals(1, result.getVehicleId());
        assertEquals("Sedan", result.getCarType());
        assertEquals("Model X", result.getModel());
        assertEquals("Available", result.getAvailabilityStatus());
        assertEquals("ABC123", result.getRegistrationNumber());
        assertEquals("Petrol", result.getFuelType());
        assertEquals("Car Model X", result.getCarModel());
        assertEquals(4, result.getSeatingCapacity());
    }

    @Test
    void testGetById_WhenVehicleDoesNotExist() throws SQLException {
        // Mock the ResultSet to return no results
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(false);  // Simulate no results

        // Mock the statement and query
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SELECT * FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        // Call getById with a non-existing ID and assert the result is null
        VehicleEntity result = vehicleDaoImpl.getById(999);
        assertNull(result);
    }

    @Test
    void testUpdateVehicle() throws SQLException {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "Sedan", "Model X", "Available", "ABC123", "Petrol", "Car Model X", 4);

        // Mock the PreparedStatement for update
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("UPDATE vehicle SET carType = ?, model = ?, availabilityStatus = ?, registrationNumber = ?, fuelType = ?, carModel = ?, seatingCapacity = ? WHERE vehicleId = ?")).thenReturn(mockStmt);

        // Simulate successful execution
        when(mockStmt.executeUpdate()).thenReturn(1);

        // Call the updateVehicle method and assert it returns true
        boolean result = vehicleDaoImpl.updateVehicle(vehicleEntity);
        assertTrue(result);

        // Verify the correct methods were called
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testDeleteVehicle_WhenVehicleExists() throws SQLException {
        // Mock the statement to check if the vehicle exists
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Vehicle exists

        // Mock the delete query execution
        when(mockConnection.prepareStatement("DELETE FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);  // Simulate a successful delete

        // Call deleteVehicle and assert it returns true
        boolean result = vehicleDaoImpl.deleteVehicle(1);
        assertTrue(result);

        // Verify the correct methods were called
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testDeleteVehicle_WhenVehicleDoesNotExist() throws SQLException {
        // Mock the statement to check if the vehicle exists
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0); // Vehicle does not exist

        // Call deleteVehicle and assert it returns false
        boolean result = vehicleDaoImpl.deleteVehicle(999);
        assertFalse(result);
    }

    @Test
    void testExistsById_WhenVehicleExists() throws SQLException {
        // Mock the statement to check if the vehicle exists
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Vehicle exists

        // Call existsById and assert it returns true
        boolean result = vehicleDaoImpl.existsById(1);
        assertTrue(result);
    }

    @Test
    void testExistsById_WhenVehicleDoesNotExist() throws SQLException {
        // Mock the statement to check if the vehicle exists
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM vehicle WHERE vehicleId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0); // Vehicle does not exist

        // Call existsById and assert it returns false
        boolean result = vehicleDaoImpl.existsById(999);
        assertFalse(result);
    }
}
