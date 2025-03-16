package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class CustomerServletTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    private CustomerServlet customerServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        customerServlet = new CustomerServlet();
        customerServlet.customerService = customerService;

        // Mock response.getWriter() to return a valid PrintWriter
        when(response.getWriter()).thenReturn(writer);

        // Mock the getReader() method to return a BufferedReader containing JSON data
        String jsonData = "{ \"customerId\": 1, \"userId\": 1, \"registrationDate\": \"2025-03-11\", \"nic\": \"1234567890\", \"name\": \"John Doe\", \"address\": \"123 Main St\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"1234567890\" }";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(request.getReader()).thenReturn(reader);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDoPost_Success() throws Exception {
        // Mock methods
        when(customerService.existsById(1)).thenReturn(false);
        when(customerService.addCustomer(any(CustomerDTO.class))).thenReturn(true);

        // Call the method to be tested
        customerServlet.doPost(request, response);

        // Verify the results
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write("Customer added successfully.");
    }

    @Test
    void testDoPost_CustomerAlreadyExists() throws Exception {
        // Mock methods
        when(customerService.existsById(1)).thenReturn(true);

        // Call the method to be tested
        customerServlet.doPost(request, response);

        // Verify the results
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
        verify(writer).write("Error: Customer ID already exists.");
    }

    @Test
    void testDoGet_CustomerFound() throws Exception {
        // Create CustomerDTO using Builder pattern
        CustomerDTO customer = new CustomerDTO.Builder()
                .customerId(1)
                .userId(1)
                .name("John Doe")
                .address("123 Main St")
                .nic("1234567890")
                .phoneNumber("1234567890")
                .registrationDate("2025-03-11")
                .email("john.doe@example.com")
                .build();

        when(request.getParameter("customerId")).thenReturn("1");
        when(customerService.getCustomerById(1)).thenReturn(customer);

        customerServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write(JsonUtils.convertDtoToJson(customer));
    }

    @Test
    void testDoGet_CustomerNotFound() throws Exception {
        when(request.getParameter("customerId")).thenReturn("1");
        when(customerService.getCustomerById(1)).thenReturn(null);

        customerServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(writer).write("{\"Error\" : \"Customer not found.\"}");
    }

    @Test
    void testDoPut_Success() throws Exception {
        // Mock methods
        String jsonData = "{ \"customerId\": 1, \"userId\": 1, \"registrationDate\": \"2025-03-11\", \"nic\": \"1234567890\", \"name\": \"John Doe\", \"address\": \"123 Main St\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"1234567890\" }";
        BufferedReader reader = new BufferedReader(new StringReader(jsonData));
        when(request.getReader()).thenReturn(reader);
        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(true);

        customerServlet.doPut(request, response);

        verify(writer).write("Customer updated successfully!");
    }

    @Test
    void testDoDelete_Success() throws Exception {
        when(request.getParameter("customerId")).thenReturn("1");
        when(customerService.deleteCustomer(1)).thenReturn(true);

        customerServlet.doDelete(request, response);

        verify(writer).write("Customer deleted successfully!");
    }

    @Test
    void testDoDelete_CustomerNotFound() throws Exception {
        when(request.getParameter("customerId")).thenReturn("1");
        when(customerService.deleteCustomer(1)).thenReturn(false);

        customerServlet.doDelete(request, response);

        verify(writer).write("Error deleting customer.");
    }
}
