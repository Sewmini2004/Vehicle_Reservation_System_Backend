package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.exception.DateFormatException;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.factory.BookingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.utils.DateFormatUtils;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        try {
            bookingService = BookingServiceFactory.createBookingService();
        } catch (SQLException throwables) {
            System.out.println("LOGG:Booking service creation with service factory");
            throwables.printStackTrace();
        }
    }

    // CREATE (POST) - Add a new booking
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);

            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int vehicleId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "vehicleId"));
            int driverId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "driverId"));
            String pickupLocation = JsonUtils.extractJsonValue(jsonData, "pickupLocation");
            String dropLocation = JsonUtils.extractJsonValue(jsonData, "dropLocation");
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            double totalBill = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "totalBill"));

            BookingDTO bookingDTO = new BookingDTO(0, customerId, vehicleId, driverId, pickupLocation, dropLocation, new Date(), carType, totalBill);

            if (bookingService.addBooking(bookingDTO)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Booking successfully created.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error creating booking.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid booking data: " + e.getMessage());
        }
    }

    // READ (GET) - Fetch a specific booking by ID or list all bookings
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String bookingIdParam = request.getParameter("bookingId");

            if (bookingIdParam != null && !bookingIdParam.isEmpty()) {
                // Fetch a single booking by ID
                int bookingId = Integer.parseInt(bookingIdParam);
                BookingDTO booking = bookingService.getBookingById(bookingId);

                if (booking != null) {
                    // Set the response content type to JSON
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    // Convert the booking DTO to JSON
                    response.getWriter().write(JsonUtils.convertDtoToJson(booking));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Booking not found.\"}");
                }
            } else {
                // Fetch all bookings if no ID is provided
                List<BookingDTO> bookings = bookingService.getAllBookings();

                if (!bookings.isEmpty()) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    // Convert the list of bookings to JSON
                    response.getWriter().write(JsonUtils.convertDtoToJson(bookings));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No bookings available.\"}");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Error\" : \"Invalid booking ID format.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving booking details.\"}");
        }
    }

    // UPDATE (PUT) - Modify booking details (Allowed within 2 hours)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);

            int bookingId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "bookingId"));

            // Fetch existing booking details to retain the original booking date
            BookingDTO existingBooking = bookingService.getBookingById(bookingId);
            if (existingBooking == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Booking not found.");
                return;
            }

            // Extract updated details from JSON
            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int vehicleId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "vehicleId"));
            int driverId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "driverId"));
            String pickupLocation = JsonUtils.extractJsonValue(jsonData, "pickupLocation");
            String dropLocation = JsonUtils.extractJsonValue(jsonData, "dropLocation");
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            double totalBill = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "totalBill"));

            Date originalBookingDate  = DateFormatUtils.toDate(JsonUtils.extractJsonValue(jsonData, "bookingDate"));

            BookingDTO updatedBooking = new BookingDTO(bookingId, customerId, vehicleId, driverId, pickupLocation, dropLocation, originalBookingDate, carType, totalBill);

            // Update the booking in the system
            if (bookingService.updateBooking(updatedBooking)) {
                response.getWriter().write("Booking updated successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Booking cannot be updated after 2 hours.");
            }
        } catch (DateFormatException ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{ \"Message\": "+ ex.getMessage() + "}");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating booking: " + e.getMessage());
        }
    }

    // DELETE (DELETE) - Remove a booking (Allowed within 4 hours)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));

            if (bookingService.deleteBooking(bookingId)) {
                response.getWriter().write("Booking deleted successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Booking cannot be deleted after 4 hours.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error deleting booking.");
        }
    }
}
