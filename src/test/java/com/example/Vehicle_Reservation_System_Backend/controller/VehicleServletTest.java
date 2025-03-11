package com.example.Vehicle_Reservation_System_Backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class VehicleServletTest {

    private VehicleServlet vehicleServlet;
    private VehicleService vehicleServiceMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private PrintWriter writerMock;

    @BeforeEach
    public void setUp() throws Exception {
        vehicleServiceMock = mock(VehicleService.class);
        vehicleServlet = new VehicleServlet();
        vehicleServlet.vehicleService = vehicleServiceMock;

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        writerMock = mock(PrintWriter.class);

        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    public void testCreateVehicle_ValidRequest() throws Exception {
        String json = "{\"carType\":\"Sedan\",\"model\":\"Toyota Camry\",\"availabilityStatus\":\"Available\",\"registrationNumber\":\"ABC123\",\"fuelType\":\"Petrol\",\"carModel\":\"2022\",\"seatingCapacity\":\"5\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(vehicleServiceMock.addVehicle(any(VehicleDTO.class))).thenReturn(true);

        vehicleServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_CREATED); // Ensure setStatus(201)
        verify(writerMock).write("Vehicle added successfully."); // Update to match the actual response message
    }

    @Test
    public void testCreateVehicle_MissingRequiredFields() throws Exception {
        String json = "{\"carType\":\"Sedan\",\"model\":\"Toyota Camry\",\"availabilityStatus\":\"Available\",\"fuelType\":\"Petrol\",\"carModel\":\"2022\",\"seatingCapacity\":\"5\"}";  // Missing registrationNumber

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        vehicleServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ensure setStatus(400)
        verify(writerMock).write("Missing required fields."); // Update to match the actual response message
    }

    @Test
    public void testGetVehicle_ValidId() throws Exception {
        int vehicleId = 1;
        VehicleDTO vehicle = new VehicleDTO(vehicleId, "Sedan", "Toyota Camry", "Available", "ABC123", "Petrol", "2022", 5);
        when(requestMock.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        when(vehicleServiceMock.getVehicleById(vehicleId)).thenReturn(vehicle);

        vehicleServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write(anyString());  // Verify that the vehicle is written as JSON
    }

    @Test
    public void testGetVehicle_InvalidIdFormat() throws Exception {
        when(requestMock.getParameter("vehicleId")).thenReturn("invalid_id");

        vehicleServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ensure setStatus(400)
        verify(writerMock).write("{\"Error\" : \"Invalid vehicle ID format.\"}"); // Update to match the actual JSON response format
    }


    @Test
    public void testGetAllVehicles() throws Exception {
        List<VehicleDTO> vehicles = Arrays.asList(
                new VehicleDTO(1, "Sedan", "Toyota Camry", "Available", "ABC123", "Petrol", "2022", 5),
                new VehicleDTO(2, "SUV", "Honda CRV", "Not Available", "XYZ456", "Diesel", "2021", 7)
        );
        when(vehicleServiceMock.getAllVehicles()).thenReturn(vehicles);

        vehicleServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write(anyString());  // Verify that the vehicles are written as JSON
    }

    @Test
    public void testDeleteVehicle_ValidId() throws Exception {
        int vehicleId = 1;
        when(requestMock.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        when(vehicleServiceMock.deleteVehicle(vehicleId)).thenReturn(true);

        vehicleServlet.doDelete(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write("Vehicle deleted successfully!"); // Update to match the actual response message
    }

    @Test
    public void testDeleteVehicle_VehicleNotFound() throws Exception {
        int vehicleId = 1;
        when(requestMock.getParameter("vehicleId")).thenReturn(String.valueOf(vehicleId));
        when(vehicleServiceMock.deleteVehicle(vehicleId)).thenReturn(false);

        vehicleServlet.doDelete(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND); // Ensure setStatus(404)
        verify(writerMock).write("Vehicle not found."); // Update to match the actual response message
    }

    @Test
    public void testUpdateVehicle_ValidUpdate() throws Exception {
        String json = "{\"vehicleId\":1,\"carType\":\"Sedan\",\"model\":\"Toyota Camry\",\"availabilityStatus\":\"Available\",\"registrationNumber\":\"ABC123\",\"fuelType\":\"Petrol\",\"carModel\":\"2022\",\"seatingCapacity\":\"5\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(vehicleServiceMock.updateVehicle(any(VehicleDTO.class))).thenReturn(true);

        vehicleServlet.doPut(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK); // Ensure setStatus(200)
        verify(writerMock).write("Vehicle updated successfully!"); // Update to match the actual response message
    }

    @Test
    public void testUpdateVehicle_InvalidData() throws Exception {
        String json = "{\"vehicleId\":1,\"carType\":\"Sedan\",\"model\":\"Toyota Camry\",\"availabilityStatus\":\"Available\",\"registrationNumber\":\"ABC123\",\"fuelType\":\"Petrol\",\"carModel\":\"2022\",\"seatingCapacity\":\"NaN\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        vehicleServlet.doPut(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Ensure setStatus(500)
        verify(writerMock).write("Error updating vehicle."); // Update to match the actual response message
    }
}
