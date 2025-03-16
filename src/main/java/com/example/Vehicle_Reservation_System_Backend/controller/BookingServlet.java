package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.exception.DateFormatException;
import com.example.Vehicle_Reservation_System_Backend.factory.CustomerServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.factory.VehicleServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.factory.BookingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;
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
    private VehicleService vehicleService;
    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        try {
            vehicleService = VehicleServiceFactory.getVehicleService();
            bookingService = BookingServiceFactory.createBookingService();
            customerService = CustomerServiceFactory.getCustomerService();
        } catch (SQLException throwables) {
            System.out.println("LOGG:Booking service creation with service factory");
            System.out.println("LOGG:vehicle service creation with service factory");
            throwables.printStackTrace();
        }
    }


    // CREATE (POST) - Add a new booking
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve JSON data from the request
            String jsonData = JsonUtils.getJsonFromRequest(request);
            System.out.println("jsonData:" + jsonData);

            // Parse and extract vehicleId from the request data
            int vehicleId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "vehicleId"));
            // Ensure that the vehicle exists in the database
            if (!vehicleService.existsById(vehicleId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"Error\": \"Vehicle ID not found.\"}");
                return;
            }

            // Extract other booking details from the request
            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int driverId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "driverId"));
            String pickupLocation = JsonUtils.extractJsonValue(jsonData, "pickupLocation");
            String dropLocation = JsonUtils.extractJsonValue(jsonData, "dropLocation");
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            String paymentMethod = JsonUtils.extractJsonValue(jsonData, "paymentMethod");
            String paymentStatus = JsonUtils.extractJsonValue(jsonData, "paymentStatus");
            double discountAmount = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "discountAmount"));
            double taxAmount = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "taxAmount"));

            // Handle invalid totalBill input
            double totalBill = 0.0;
            double distance = 0.0;
            try {
                totalBill = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "totalBill"));
                distance = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "distance"));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"Error\": \"Invalid number format: " + e.getMessage() + "\"}");
                return;
            }

            // Check if customer exists
            CustomerDTO customerDTO = customerService.getCustomerById(customerId);
            if (customerDTO == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"Error\": \"Customer not found with ID: " + customerId + "\"}");
                return;
            }

            // Create a BookingDTO object from the extracted data

            BookingDTO bookingDTO = new BookingDTO.Builder()
                    .bookingId(0)  // default value
                    .customerId(customerId)
                    .vehicleId(vehicleId)
                    .driverId(driverId)
                    .pickupLocation(pickupLocation)
                    .dropLocation(dropLocation)
                    .bookingDate(new Date()) // Default to current date if needed
                    .carType(carType)
                    .totalBill(totalBill)
                    .cancelStatus("") // Default value or empty string if needed
                    .distance(distance)
                    .customerName("")
                    .driverName("")
                    .vehicleModel("")
                    .vehicleRegistrationNumber("")
                    .build();

            // Create a BillingDTO object and set relevant values
            BillingDTO billingDTO = new BillingDTO();
            billingDTO.setPaymentMethod(paymentMethod);
            billingDTO.setPaymentStatus(paymentStatus);

            // Here we set the totalAmount, discountAmount, taxAmount, finalAmount
            billingDTO.setTotalAmount(totalBill);  // Assuming no discount and tax, set it as the totalBill
            billingDTO.setDiscountAmount(discountAmount);    // If there's a discount, adjust here
            billingDTO.setTaxAmount(taxAmount);         // If there's tax, adjust here
            billingDTO.setFinalAmount(totalBill); // Final amount after applying any discounts/taxes

            // Set the billing details in the bookingDTO
            bookingDTO.setBillingDetails(billingDTO);

            // Call the service method to add the booking
            boolean isBookingCreated = bookingService.addBooking(bookingDTO);
            if (isBookingCreated) {
                // Booking created successfully
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                response.getWriter().write("{\"Message\": \"Booking successfully created.\"}");

                // Generate the confirmation email
                sendConfirmationEmail(customerDTO, bookingDTO);

                // Generate eBill content
                generateBill(billingDTO);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"Error\": \"Error creating booking.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Invalid booking data: " + e.getMessage() + "\"}");
        }
    }

    // Send confirmation email to the customer
    private void sendConfirmationEmail(CustomerDTO customerDTO, BookingDTO bookingDTO) {
        String emailContent = "Dear " + customerDTO.getName() + ",\n\n" +
                "Thank you for registering with our Mega City Cab service!\n\n" +
                "We are pleased to confirm your booking. Your details are as follows:\n" +
                "Name: " + customerDTO.getName() + "\n" +
                "Email: " + customerDTO.getEmail() + "\n" +
                "Phone: " + customerDTO.getPhoneNumber() + "\n" +
                "Booking Details:\n" +
                "Pickup Location: " + bookingDTO.getPickupLocation() + "\n" +
                "Drop Location: " + bookingDTO.getDropLocation() + "\n" +
                "Booking Date: " + bookingDTO.getBookingDate() + "\n\n" +
                "Best Regards,\nYour Service Team";

        System.out.println("Confirmation Email Content:");
        System.out.println(emailContent);
    }

    // Generate eBill after booking creation
    private void generateBill(BillingDTO billingDTO) {
        String billContent = "Billing Details:\n" +
                "Total Amount: " + billingDTO.getTotalAmount() + "\n" +
                "Discount: " + billingDTO.getDiscountAmount() + "\n" +
                "Tax Amount: " + billingDTO.getTaxAmount() + "\n" +
                "Final Amount: " + billingDTO.getFinalAmount() + "\n" +
                "Payment Method: " + billingDTO.getPaymentMethod() + "\n" +
                "Payment Status: " + billingDTO.getPaymentStatus() + "\n\n" +
                "Thank you for choosing our service!\n\n" +
                "Best Regards,\nYour Service Team";

        System.out.println("eBill Content:");
        System.out.println(billContent);
    }



    // READ (GET) - Fetch a specific booking by ID or list all bookings
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String searchTerm = request.getParameter("search");  // Get search term from the request
            String bookingIdParam = request.getParameter("bookingId");

            // If search term is present, filter the bookings based on it
            if (searchTerm != null && !searchTerm.isEmpty()) {
                // Fetch filtered bookings by search term
                List<BookingDTO> bookings = bookingService.searchBookings(searchTerm);

                if (!bookings.isEmpty()) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(bookings)); // Send filtered bookings as response
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No bookings found.\"}");
                }
            } else if (bookingIdParam != null && !bookingIdParam.isEmpty()) {
                // Fetch a specific booking if bookingId is provided
                int bookingId = Integer.parseInt(bookingIdParam);
                BookingDTO booking = bookingService.getBookingById(bookingId);

                if (booking != null) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(booking));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Booking not found.\"}");
                }
            } else {
                // Fetch all bookings if no search or bookingId is provided
                List<BookingDTO> bookings = bookingService.getAllBookings();

                if (!bookings.isEmpty()) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
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
            double distance = Double.parseDouble(JsonUtils.extractJsonValue(jsonData, "distance"));

            Date originalBookingDate = DateFormatUtils.toDate(JsonUtils.extractJsonValue(jsonData, "bookingDate"));

          //  BookingDTO updatedBooking = new BookingDTO(bookingId, customerId, vehicleId, driverId, pickupLocation, dropLocation, originalBookingDate, carType, totalBill, "", distance);
            BookingDTO updatedBooking = new BookingDTO.Builder()
                    .bookingId(bookingId)
                    .customerId(customerId)
                    .vehicleId(vehicleId)
                    .driverId(driverId)
                    .pickupLocation(pickupLocation)
                    .dropLocation(dropLocation)
                    .bookingDate(new Date())
                    .carType(carType)
                    .totalBill(totalBill)
                    .cancelStatus("")
                    .distance(distance)
                    .build();
            // Update the booking in the system
            if (bookingService.updateBooking(updatedBooking)) {
                response.setStatus(HttpServletResponse.SC_OK);  // Ensure status is set to 200 OK
                response.getWriter().write("Booking updated successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Booking cannot be updated after 2 hours.");
            }
        } catch (DateFormatException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{ \"Message\": \"" + ex.getMessage() + "\"}");
        } catch (Exception e) {
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
                response.setStatus(HttpServletResponse.SC_OK);  // Ensure the status is set to 200 OK
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
