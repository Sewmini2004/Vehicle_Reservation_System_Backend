package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import com.example.Vehicle_Reservation_System_Backend.service.RegisterService;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

class LoginServletTest {

    private LoginServlet loginServlet;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private RegisterService registerServiceMock;

    @Mock
    private PrintWriter writerMock;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initializes mocks
        loginServlet = new LoginServlet();
        loginServlet.registerService = registerServiceMock; // Inject the mock service

        // Set up the PrintWriter mock to be returned by responseMock.getWriter()
        writerMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    void testMissingJsonRequestBody() throws IOException, ServletException {
        // Simulate the absence of a JSON request body
        when(requestMock.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader("")));

        // Mock the PrintWriter to capture the output
        writerMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(writerMock);
        loginServlet.doPost(requestMock, responseMock);
        // Verify that the status code is set to 400 (Bad Request)
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writerMock).write("{\"Error\": \"Missing JSON request body.\"}");
    }


    @Test
    void testMissingUsernameOrPassword() throws IOException, ServletException {
        String json = "{}"; // Empty JSON
        when(requestMock.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));

        loginServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(responseMock.getWriter()).write("{\"Error\": \"Username and password are required.\"}");
    }

    @Test
    void testInvalidLogin() throws IOException, ServletException {
        String json = "{\"username\":\"user\",\"password\":\"wrongpassword\"}";
        when(requestMock.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));

        // Mock the service to return null (user not found)
        when(registerServiceMock.getUserByUsername("user")).thenReturn(null);

        loginServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(responseMock.getWriter()).write("{\"Error\": \"Invalid username or password.\"}");
    }

    @Test
    void testSuccessfulLogin() throws IOException, ServletException {
        String json = "{\"username\":\"user\",\"password\":\"correctpassword\"}";
        when(requestMock.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));

        // Mock the service to return a user with the correct password
        RegisterDTO mockUser = new RegisterDTO(1, "user", "correctpassword", "John", "Doe", "johndoe@example.com");
        when(registerServiceMock.getUserByUsername("user")).thenReturn(mockUser);

        loginServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(responseMock.getWriter()).write("{\"Message\": \"Login successful.\"}");
    }


    @Test
    void testErrorDuringProcessing() throws IOException, ServletException {
        String json = "{\"username\":\"user\",\"password\":\"password\"}";
        when(requestMock.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));

        // Simulate an exception in the service
        when(registerServiceMock.getUserByUsername("user")).thenThrow(new RuntimeException("Database error"));

        loginServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(responseMock.getWriter()).write("{\"Error\": \"An error occurred while processing the request.\"}");
    }
}
