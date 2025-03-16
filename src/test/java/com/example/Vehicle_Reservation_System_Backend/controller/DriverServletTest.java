package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.mockito.Mockito.*;

public class DriverServletTest {

    private DriverServlet driverServlet;
    private DriverService driverServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private PrintWriter writerMock;

    @BeforeEach
    public void setUp() throws Exception {
        driverServiceMock = mock(DriverService.class);
        driverServlet = new DriverServlet();
        driverServlet.driverService = driverServiceMock;

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        writerMock = mock(PrintWriter.class);

        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    public void testCreateDriver_ValidRequest() throws Exception {
        String json = "{\"name\":\"John Doe\",\"licenseNumber\":\"AB12345\",\"status\":\"active\",\"shiftTiming\":\"Morning\",\"phoneNumber\":\"1234567890\",\"salary\":\"3000\",\"experienceYears\":\"5\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(driverServiceMock.addDriver(any(DriverDTO.class))).thenReturn(true);

        driverServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CREATED); // Ensure setStatus(201)
        verify(writerMock).write("{\"Message\" : \"Driver added successfully.\"}");
    }

    @Test
    public void testCreateDriver_MissingRequiredFields() throws Exception {
        String json = "{\"name\":\"John Doe\",\"licenseNumber\":\"AB12345\",\"status\":\"active\",\"shiftTiming\":\"Morning\",\"salary\":\"3000\",\"experienceYears\":\"5\"}";  // Missing phoneNumber

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        driverServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ensure setStatus(400)
        verify(writerMock).write("{\"Error\" : \"Missing required fields.\"}");
    }

    @Test
    public void testCreateDriver_InvalidDataFormat() throws Exception {
        String json = "{\"name\":\"John Doe\",\"licenseNumber\":\"AB12345\",\"status\":\"active\",\"shiftTiming\":\"Morning\",\"phoneNumber\":\"1234567890\",\"salary\":\"NaN\",\"experienceYears\":\"five\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        driverServlet.doPost(requestMock, responseMock);

        // The error message now reflects the actual issue with the input "five"
        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Ensure setStatus(500)
        verify(writerMock).write("{\"Error\" : \"Error adding driver: For input string: \"five\"\"}");
    }

    @Test
    public void testGetDriver_ValidId() throws Exception {
        int driverId = 1;
        DriverDTO driver = new DriverDTO.Builder()
                .driverId(driverId)
                .name("John Doe")
                .licenseNumber("AB12345")
                .status("active")
                .shiftTiming("Morning")
                .salary(3000)
                .experienceYears(5)
                .phoneNumber("1234567890")
                .build();

        when(requestMock.getParameter("driverId")).thenReturn(String.valueOf(driverId));
        when(driverServiceMock.getDriverById(driverId)).thenReturn(driver);

        driverServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write(anyString());  // Verify that the driver is written as JSON
    }

    @Test
    public void testGetDriver_InvalidIdFormat() throws Exception {
        when(requestMock.getParameter("driverId")).thenReturn("invalid_id");

        driverServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ensure setStatus(400)
        verify(writerMock).write("{\"Error\" : \"Invalid driver ID format.\"}");
    }

    @Test
    public void testDeleteDriver_ValidId() throws Exception {
        int driverId = 1;
        when(requestMock.getParameter("driverId")).thenReturn(String.valueOf(driverId));
        when(driverServiceMock.deleteDriver(driverId)).thenReturn(true);

        driverServlet.doDelete(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write("{\"Message\" : \"Driver deleted successfully!\"}");
    }

    @Test
    public void testDeleteDriver_DriverNotFound() throws Exception {
        int driverId = 1;
        when(requestMock.getParameter("driverId")).thenReturn(String.valueOf(driverId));
        when(driverServiceMock.deleteDriver(driverId)).thenReturn(false);

        driverServlet.doDelete(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND); // Ensure setStatus(404)
        verify(writerMock).write("{\"Error\" : \"Driver not found.\"}");
    }

    @Test
    public void testPutDriver_ValidUpdate() throws Exception {
        String json = "{\"driverId\":1,\"name\":\"John Updated\",\"licenseNumber\":\"AB12345\",\"status\":\"active\",\"shiftTiming\":\"Morning\",\"salary\":\"3500\",\"experienceYears\":\"6\",\"phoneNumber\":\"1234567890\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(driverServiceMock.updateDriver(any(DriverDTO.class))).thenReturn(true);

        driverServlet.doPut(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write("{\"Message\" : \"Driver updated successfully!\"}");
    }

    @Test
    public void testPutDriver_InvalidData() throws Exception {
        String json = "{\"driverId\":1,\"name\":\"John Updated\",\"licenseNumber\":\"AB12345\",\"status\":\"active\",\"shiftTiming\":\"Morning\",\"salary\":\"NaN\",\"experienceYears\":\"six\",\"phoneNumber\":\"1234567890\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        driverServlet.doPut(requestMock, responseMock);

        // The error message now reflects the actual issue with the input "six"
        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Ensure setStatus(500)
        verify(writerMock).write("{\"Error\" : \"Error updating driver: For input string: \"six\"\"}");
    }
}
