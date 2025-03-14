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
        customerDaoMock = mock(CustomerDao.class); // Mock the CustomerDao
        customerService = new CustomerServiceImpl(customerDaoMock); // Pass the mock to the service
    }

    @AfterEach
    void tearDown() {
        // Clean up resources after each test, if needed
    }

    @Test
    void testAddCustomerValidData() throws SQLException {
        // Create a valid CustomerDTO object with all the fields populated
        CustomerDTO customerDTO = new CustomerDTO(
                1,  // customerId
                101, // userId
                "John Doe", // name
                "123 Main St", // address
                "199012345678", // NIC
                "0712345678", // phoneNumber
                "2025-03-12", // registrationDate
                "john.doe@example.com" // email
        );

        // Mock the behavior of customerDao
        when(customerDaoMock.saveCustomer(any(CustomerEntity.class))).thenReturn(true);

        // Test the addCustomer method
        boolean result = customerService.addCustomer(customerDTO);

        assertTrue(result); // Assert that the customer was added successfully
    }

    @Test
    void testAddCustomerInvalidEmail() {
        // Create a CustomerDTO with invalid email
        CustomerDTO customerDTO = new CustomerDTO(
                1, 101, "John Doe", "123 Main St", "199012345678", "0712345678", "2025-03-12", "invalid-email"
        );

        // Assert that adding a customer with an invalid email throws an exception
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customerDTO));
    }

    @Test
    void testAddCustomerInvalidNIC() {
        // Create a CustomerDTO with invalid NIC
        CustomerDTO customerDTO = new CustomerDTO(
                1, 101, "John Doe", "123 Main St", "invalid-nic", "0712345678", "2025-03-12", "john.doe@example.com"
        );

        // Assert that adding a customer with an invalid NIC throws an exception
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(customerDTO));
    }

    @Test
    void testGetCustomerById() {
        // Create a mock CustomerEntity with valid data
        CustomerEntity mockCustomerEntity = new CustomerEntity(1, 101,"John Doe", "123 Main St", "199012345678", "0712345678", "2025-03-12", "john.doe@example.com");
        when(customerDaoMock.getById(1)).thenReturn(mockCustomerEntity);

        // Test the getCustomerById method
        CustomerDTO result = customerService.getCustomerById(1);

        assertNotNull(result); // Assert that the result is not null
        assertEquals("John Doe", result.getName()); // Assert that the customer name is correct
        assertEquals("john.doe@example.com", result.getEmail()); // Assert that the customer email is correct
    }



    @Test
    void testGetCustomerByIdNotFound() {
        // Mock the behavior of customerDao for a non-existing customer
        when(customerDaoMock.getById(999)).thenReturn(null); // Returning null to simulate customer not found

        // Test that the getCustomerById method throws NotFoundException for a non-existing customer
        assertThrows(NotFoundException.class, () -> customerService.getCustomerById(999));
    }



    @Test
    void testUpdateCustomer() {
        // Create a valid CustomerDTO object
        CustomerDTO customerDTO = new CustomerDTO(
                1, 101, "John Doe", "123 Main St", "199012345678", "0712345678", "2025-03-12", "john.doe@example.com"
        );

        // Mock the behavior of customerDao
        when(customerDaoMock.existsById(1)).thenReturn(true);
        when(customerDaoMock.updateCustomer(any(CustomerEntity.class))).thenReturn(true);

        // Test the updateCustomer method
        boolean result = customerService.updateCustomer(customerDTO);

        assertTrue(result); // Assert that the customer was updated successfully
    }

    @Test
    void testUpdateCustomerNotFound() {
        // Create a valid CustomerDTO object
        CustomerDTO customerDTO = new CustomerDTO(
                1, 101, "John Doe", "123 Main St", "199012345678", "0712345678", "2025-03-12", "john.doe@example.com"
        );

        // Mock the behavior of customerDao
        when(customerDaoMock.existsById(1)).thenReturn(false);

        // Test that the updateCustomer method throws NotFoundException
        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(customerDTO));
    }

    @Test
    void testDeleteCustomer() {
        // Mock the behavior of customerDao
        when(customerDaoMock.deleteCustomer(1)).thenReturn(true);

        // Test the deleteCustomer method
        boolean result = customerService.deleteCustomer(1);

        assertTrue(result); // Assert that the customer was deleted successfully
    }



    @Test
    void testDeleteCustomerNotFound() {
        // Mock the behavior of customerDao
        when(customerDaoMock.deleteCustomer(999)).thenReturn(false); // Simulating that the customer does not exist

        // Test that the deleteCustomer method throws NotFoundException for a non-existing customer
        assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(999));
    }

}
