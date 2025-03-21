package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingServletTest {

    @InjectMocks
    private BookingServlet bookingServlet;

    @Mock
    private BookingService bookingServiceMock;

    @Mock
    private VehicleService vehicleServiceMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(responseMock.getWriter()).thenReturn(writerMock);
    }


    @Test
    void testCreateBooking_VehicleNotFound() throws Exception {
        String json = "{\"vehicleId\":\"999\",\"customerId\":\"1\",\"driverId\":\"1\",\"pickupLocation\":\"A\",\"dropLocation\":\"B\",\"carType\":\"Sedan\",\"totalBill\":\"150.0\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(vehicleServiceMock.existsById(anyInt())).thenReturn(false);

        bookingServlet.doPost(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writerMock).write("{\"Error\": \"Vehicle ID not found.\"}");
    }

    @Test
    void testGetBooking_ValidId() throws Exception {
        int bookingId = 1;
        BookingDTO booking = new BookingDTO.Builder()
                .bookingId(bookingId)
                .bookingDate(new Date())
                .driverId(1)
                .vehicleId(1)
                .customerId(1)
                .build();
        when(requestMock.getParameter("bookingId")).thenReturn(String.valueOf(bookingId));
        when(bookingServiceMock.getBookingById(bookingId)).thenReturn(booking);

        bookingServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(writerMock).write(anyString());
    }

    @Test
    void testGetBooking_BookingNotFound() throws Exception {
        int bookingId = 999;
        when(requestMock.getParameter("bookingId")).thenReturn(String.valueOf(bookingId));
        when(bookingServiceMock.getBookingById(bookingId)).thenReturn(null);

        bookingServlet.doGet(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND); // Ensure not found status (404)
        verify(writerMock).write("{\"Error\" : \"Booking not found.\"}"); // Ensure error message
    }


    @Test
    void testUpdateBooking_ValidUpdate() throws Exception {
        // Create a JSON string for the update request
        String json = "{\"bookingId\":1,\"customerId\":\"1\",\"vehicleId\":\"1\",\"driverId\":\"1\",\"pickupLocation\":\"A\",\"dropLocation\":\"B\",\"carType\":\"Sedan\",\"totalBill\":\"150.0\",\"bookingDate\":\"2025-03-11\"}";

        // Mocking the request reader to return the JSON string
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        // Create a valid BookingDTO to be returned by the mocked booking service
        BookingDTO bookingDTO = new BookingDTO.Builder()
                .bookingId(1)
                .customerId(1)
                .vehicleId(1)
                .driverId(1)
                .pickupLocation("A")
                .dropLocation("B")
                .carType("Sedan")
                .totalBill(150.0)
                .bookingDate(new java.util.Date())
                .cancelStatus("")
                .distance(0.0)
                .build();

        when(bookingServiceMock.getBookingById(anyInt())).thenReturn(bookingDTO);

        when(bookingServiceMock.updateBooking(any(BookingDTO.class))).thenReturn(true);

        PrintWriter writer = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(writer);

        bookingServlet.doPut(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_OK);  // Ensure status is 200 OK
    }


    @Test
    void testUpdateBooking_BookingNotFound() throws Exception {
        String json = "{\"bookingId\":999,\"customerId\":\"1\",\"vehicleId\":\"1\",\"driverId\":\"1\",\"pickupLocation\":\"A\",\"dropLocation\":\"B\",\"carType\":\"Sedan\",\"totalBill\":\"150.0\"}";
        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(bookingServiceMock.getBookingById(anyInt())).thenReturn(null);

        bookingServlet.doPut(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND); // Ensure not found status (404)
        verify(writerMock).write("Booking not found.");
    }

    @Test
    void testDeleteBooking_ValidId() throws Exception {
        int bookingId = 1;
        when(requestMock.getParameter("bookingId")).thenReturn(String.valueOf(bookingId));
        when(bookingServiceMock.deleteBooking(bookingId)).thenReturn(true);

        bookingServlet.doDelete(requestMock, responseMock);

        // Verify the correct status is set (200 OK)
        verify(responseMock).setStatus(HttpServletResponse.SC_OK);
        verify(writerMock).write("Booking deleted successfully.");
    }



    @Test
    void testDeleteBooking_BookingNotFound() throws Exception {
        int bookingId = 999;
        when(requestMock.getParameter("bookingId")).thenReturn(String.valueOf(bookingId));
        when(bookingServiceMock.deleteBooking(bookingId)).thenReturn(false);

        bookingServlet.doDelete(requestMock, responseMock);

        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writerMock).write("Booking cannot be deleted after 4 hours.");
    }

    @Test
    void testCreateBooking_InvalidData() throws Exception {
        String json = "{\"vehicleId\":\"1\",\"customerId\":\"1\",\"driverId\":\"1\",\"pickupLocation\":\"A\",\"dropLocation\":\"B\",\"carType\":\"Sedan\",\"totalBill\":\"invalidBill\"}";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        // Mock vehicleService to return true for vehicle ID check
        when(vehicleServiceMock.existsById(anyInt())).thenReturn(true);

        // Run the test method
        bookingServlet.doPost(requestMock, responseMock);

        // Verify response for invalid number format (for totalBill)
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ensure bad request status (400)
        writerMock.write("{\"Error\": \"Invalid number format: For input string: \"invalidBill\"\"}");

    }


}
