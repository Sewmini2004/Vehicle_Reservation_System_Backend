package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
import com.example.Vehicle_Reservation_System_Backend.factory.BillingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private BillingService billingService;

    @Override
    public void init() throws ServletException {
        try {
            billingService = BillingServiceFactory.createBillingService();
        } catch (SQLException e) {
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
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(billing));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Billing not found for booking ID " + bookingId + "\"}");
                }
            } else {
                List<BillingDTO> allBilling = billingService.getAllBills();

                if (!allBilling.isEmpty()) {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(allBilling));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No billing records found.\"}");
                }
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
