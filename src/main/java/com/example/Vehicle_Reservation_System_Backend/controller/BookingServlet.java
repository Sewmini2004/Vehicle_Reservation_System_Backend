package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.factory.BookingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        bookingService = BookingServiceFactory.getBookingService();
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
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Booking found: Customer " + booking.getCustomerId() + ", Vehicle " + booking.getVehicleId() + ", Total Bill: " + booking.getTotalBill());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Booking not found.");
                }
            } else {
                // Fetch all bookings if no ID is provided
                List<BookingDTO> bookings = bookingService.getAllBookings();

                if (!bookings.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    StringBuilder responseText = new StringBuilder("Booking List:\n");
                    for (BookingDTO booking : bookings) {
                        responseText.append("ID: ").append(booking.getBookingId())
                                .append(", Customer: ").append(booking.getCustomerId())
                                .append(", Vehicle: ").append(booking.getVehicleId())
                                .append(", Total Bill: ").append(booking.getTotalBill()).append("\n");
                    }
                    response.getWriter().write(responseText.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("No bookings available.");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid booking ID format.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving booking details.");
        }
    }

    // UPDATE (PUT) - Modify booking details (Allowed within 2 hours)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);

            int bookingId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "bookingId"));
            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int vehicleId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "vehicleId"));
            int driverId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "driverId"));
            String pickupLocation = JsonUtils.extractJsonValue(jsonData, "pickupLocation");
            String dropLocation = JsonUtils.extractJsonValue(jsonData, "dropLocation");
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            double totalBill = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "totalBill"));

            BookingDTO bookingDTO = new BookingDTO(bookingId, customerId, vehicleId, driverId, pickupLocation, dropLocation, new Date(), carType, totalBill);

            if (bookingService.updateBooking(bookingDTO)) {
                response.getWriter().write("Booking updated successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Booking cannot be updated after 2 hours.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating booking.");
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
