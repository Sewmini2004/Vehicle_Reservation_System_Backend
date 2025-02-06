package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.factory.CustomerServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        customerService = CustomerServiceFactory.getCustomerService();
        if (customerService == null) {
            throw new ServletException("Failed to initialize CustomerService");
        }
    }

    //  CREATE (POST) - Add a new customer using JSON
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("LOG::CustomerServlet::doPost::Started");

            // Get JSON data from request
            String jsonData = JsonUtils.getJsonFromRequest(request);

            // Extract values from JSON
            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int userId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "userId"));
            String registrationDate = JsonUtils.extractJsonValue(jsonData, "registrationDate");
            String nic = JsonUtils.extractJsonValue(jsonData, "nic");
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String address = JsonUtils.extractJsonValue(jsonData, "address");
            String email = JsonUtils.extractJsonValue(jsonData, "email");
            String phone = JsonUtils.extractJsonValue(jsonData, "phone");

            // Validate required fields
            if (name == null || email == null || phone == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required fields.");
                return;
            }

            // Create DTO object
            CustomerDTO customerDTO = new CustomerDTO(customerId, userId, name, address, nic, phone, registrationDate, email);
            System.out.println("LOG::CustomerServlet::doPost::Data Extracted");

            // Call the service
            if (customerService.addCustomer(customerDTO)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Customer added successfully.");
            }
        } catch (AlreadyException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing your request.");
        }
        System.out.println("LOG::CustomerServlet::doPost::End");
    }

    // ✅ READ (GET) - Fetch customer details by ID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            CustomerDTO customer = customerService.getCustomerById(customerId);

            if (customer != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Customer found: " + customer.getName());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Customer not found.");
            }
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing your request.");
        }
    }

    // ✅ UPDATE (PUT) - Modify customer details using JSON
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);

            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int userId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "userId"));
            String registrationDate = JsonUtils.extractJsonValue(jsonData, "registrationDate");
            String nic = JsonUtils.extractJsonValue(jsonData, "nic");
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String address = JsonUtils.extractJsonValue(jsonData, "address");
            String email = JsonUtils.extractJsonValue(jsonData, "email");
            String phone = JsonUtils.extractJsonValue(jsonData, "phone");

            CustomerDTO customerDTO = new CustomerDTO(customerId, userId, name, address, nic, phone, registrationDate, email);

            if (customerService.updateCustomer(customerDTO)) {
                response.getWriter().write("Customer updated successfully!");
            } else {
                response.getWriter().write("Error updating customer.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating customer.");
        }
    }

    // ✅ DELETE (DELETE) - Remove a customer
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));

            if (customerService.deleteCustomer(customerId)) {
                response.getWriter().write("Customer deleted successfully!");
            } else {
                response.getWriter().write("Error deleting customer.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error deleting customer.");
        }
    }
}
