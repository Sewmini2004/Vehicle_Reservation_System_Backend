package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dao.impl.BillingDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.factory.BillingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.BillingServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private BillingService billingService;

        @Override
        public void init() throws ServletException {
            try {
                // Initialize the billing service using the factory method
                billingService = BillingServiceFactory.createBillingService();
            } catch (SQLException e) {
                // Handle the exception properly
                System.out.println("Error creating BillingService using factory");
                e.printStackTrace();
                throw new ServletException("Error initializing BillingService", e);
            }
        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String bookingIdParam = request.getParameter("bookingId");

            if (bookingIdParam != null && !bookingIdParam.isEmpty()) {
                // Fetch a single billing record by booking ID
                int bookingId = Integer.parseInt(bookingIdParam);
                BillingDTO billing = billingService.getBillByBookingId(bookingId);

                if (billing != null) {
                    // Set the response content type to JSON
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    // Convert the BillingDTO to JSON
                    response.getWriter().write(JsonUtils.convertDtoToJson(billing));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Billing not found for booking ID " + bookingId + "\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Error\" : \"Booking ID is required.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Error\" : \"Invalid booking ID format.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"Database error occurred while retrieving billing details.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while processing the request.\"}");
        }
    }
}
