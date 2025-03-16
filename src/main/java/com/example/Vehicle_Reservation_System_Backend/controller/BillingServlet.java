package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.factory.BillingServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.BillingService;
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
            String searchTerm = request.getParameter("search");

            if (searchTerm != null && !searchTerm.isEmpty()) {
                // If search parameter is provided, filter payments by relevant fields
                List<BillingDTO> payments = billingService.searchPayments(searchTerm);

                if (!payments.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(payments));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No payments found.\"}");
                }
            } else {
                // No search term, return all payments
                List<BillingDTO> payments = billingService.getAllBills();
                if (!payments.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(payments));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No payments available.\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving payment details.\"}");
        }
    }

}
