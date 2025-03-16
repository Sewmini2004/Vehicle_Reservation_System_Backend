package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;
    private CustomerDao customerDaoMock;

    @BeforeEach
    void setUp() {
        customerDaoMock = mock(CustomerDao.class);
        customerService = new CustomerServiceImpl(customerDaoMock);
    }

    @AfterEach
    void tearDown() {
        // Clean up resources after each test, if needed
    }

    @Test
    void testAddCustomerValidData() throws SQLException {
        // Create a valid CustomerDTO object using the Builder pattern
        CustomerDTO customerDTO = new CustomerDTO.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("199012345678")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("john.doe@example.com")
                .build();

        when(customerDaoMock.saveCustomer(any(CustomerEntity.class))).thenReturn(true);

        boolean result = customerService.addCustomer(customerDTO);

        assertTrue(result);
    }

    @Test
    void testAddCustomerInvalidEmail() {
        // Create a CustomerDTO with invalid email using the Builder pattern
        CustomerDTO customerDTO = new CustomerDTO.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("199012345678")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("invalid-email")
                .build();

        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customerDTO));
    }

    @Test
    void testAddCustomerInvalidNIC() {
        // Create a CustomerDTO with invalid NIC using the Builder pattern
        CustomerDTO customerDTO = new CustomerDTO.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("invalid-nic")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("john.doe@example.com")
                .build();

        // Assert that adding a customer with an invalid NIC throws an exception
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customerDTO));
    }

    @Test
    void testGetCustomerById() {
        // Create a mock CustomerEntity with valid data
        CustomerEntity mockCustomerEntity = new CustomerEntity.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("199012345678")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("john.doe@example.com")
                .build();

        when(customerDaoMock.getById(1)).thenReturn(mockCustomerEntity);

        // Test the getCustomerById method
        CustomerDTO result = customerService.getCustomerById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerDaoMock.getById(999)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> customerService.getCustomerById(999));
    }

    @Test
    void testUpdateCustomer() {
        // Create a valid CustomerDTO object using the Builder pattern
        CustomerDTO customerDTO = new CustomerDTO.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("199012345678")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("john.doe@example.com")
                .build();

        // Mock the behavior of customerDao
        when(customerDaoMock.existsById(1)).thenReturn(true);
        when(customerDaoMock.updateCustomer(any(CustomerEntity.class))).thenReturn(true);

        // Test the updateCustomer method
        boolean result = customerService.updateCustomer(customerDTO);

        assertTrue(result);
    }

    @Test
    void testUpdateCustomerNotFound() {
        // Create a valid CustomerDTO object using the Builder pattern
        CustomerDTO customerDTO = new CustomerDTO.Builder()
                .customerId(1)
                .userId(101)
                .name("John Doe")
                .address("123 Main St")
                .nic("199012345678")
                .phoneNumber("0712345678")
                .registrationDate("2025-03-12")
                .email("john.doe@example.com")
                .build();

        when(customerDaoMock.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerDTO));
    }

    @Test
    void testDeleteCustomer() {
        // Mock the behavior of customerDao
        when(customerDaoMock.deleteCustomer(1)).thenReturn(true);

        // Test the deleteCustomer method
        boolean result = customerService.deleteCustomer(1);

        assertTrue(result);
    }

    @Test
    void testDeleteCustomerNotFound() {
        // Mock the behavior of customerDao
        when(customerDaoMock.deleteCustomer(999)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(999));
    }
}
