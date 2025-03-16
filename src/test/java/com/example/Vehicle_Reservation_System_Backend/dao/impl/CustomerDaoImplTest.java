package com.example.Vehicle_Reservation_System_Backend.dao.impl;

import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoImplTest {

    private Connection mockConnection;
    private CustomerDaoImpl customerDaoImpl;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);  // Mock the Connection object
        customerDaoImpl = new CustomerDaoImpl(mockConnection);  // Create an instance of CustomerDaoImpl with the mock connection
    }

    @AfterEach
    void tearDown() {
        mockConnection = null;
        customerDaoImpl = null;
    }

    @Test
    void testSaveCustomer_WhenCustomerDoesNotExist() throws SQLException {
        // Use the Builder pattern to create the customer entity
        CustomerEntity customerEntity = new CustomerEntity.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Street")
                .nic("NIC123")
                .phoneNumber("1234567890")
                .registrationDate("2025-03-12")
                .email("johndoe@example.com")
                .build();

        // Mock the statement to simulate no existing record with the given email or NIC
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM customer WHERE email = ? OR nic = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);  // Simulate that no matching records were found

        // Mock the insert query
        String insertQuery = "INSERT INTO customer (userId, name, address, nic, phoneNumber, registrationDate, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(mockConnection.prepareStatement(insertQuery)).thenReturn(mockStmt);

        // Call the saveCustomer method and verify if it returns true
        boolean result = customerDaoImpl.saveCustomer(customerEntity);
        assertTrue(result);

        // Verify the correct statements were called
        verify(mockStmt, times(1)).executeUpdate();
    }

    @Test
    void testSaveCustomer_WhenCustomerAlreadyExists() throws SQLException {
        // Use the Builder pattern to create the customer entity
        CustomerEntity customerEntity = new CustomerEntity.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Street")
                .nic("NIC123")
                .phoneNumber("1234567890")
                .registrationDate("2025-03-12")
                .email("johndoe@example.com")
                .build();

        // Mock the statement to simulate that a customer with the given email or NIC already exists
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement("SELECT COUNT(*) FROM customer WHERE email = ? OR nic = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);  // Simulate that a matching record is found

        // Assert that AlreadyException is thrown
        AlreadyException exception = assertThrows(AlreadyException.class, () -> {
            customerDaoImpl.saveCustomer(customerEntity);
        });
        assertEquals("Customer with email or NIC already exists.", exception.getMessage());
    }

    @Test
    void testGetById_WhenCustomerExists() throws SQLException {
        // Mock the ResultSet to return the customer data
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("customerId")).thenReturn(1);
        when(mockResultSet.getInt("userId")).thenReturn(101);
        when(mockResultSet.getString("name")).thenReturn("John Doe");
        when(mockResultSet.getString("address")).thenReturn("123 Street");
        when(mockResultSet.getString("nic")).thenReturn("NIC123");
        when(mockResultSet.getString("phoneNumber")).thenReturn("1234567890");
        when(mockResultSet.getString("registrationDate")).thenReturn("2025-03-12");
        when(mockResultSet.getString("email")).thenReturn("johndoe@example.com");

        // Mock the statement and query
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SELECT * FROM customer WHERE customerId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        // Create the customer entity and assert it is returned
        CustomerEntity result = customerDaoImpl.getById(1);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe@example.com", result.getEmail());
    }

    @Test
    void testGetById_WhenCustomerDoesNotExist() throws SQLException {
        // Mock the ResultSet to return no results
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(false);  // Simulate no results

        // Mock the statement and query
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement("SELECT * FROM customer WHERE customerId = ?")).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResultSet);

        // Assert that NotFoundException is thrown
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerDaoImpl.getById(999);  // Non-existing customer ID
        });
        assertEquals("Customer with ID 999 not found.", exception.getMessage());
    }
}
