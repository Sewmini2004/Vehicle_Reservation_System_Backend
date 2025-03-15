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
import java.util.List;


public class CustomerServlet extends HttpServlet {

    public CustomerService customerService;

    @Override
    public void init() throws ServletException {
        System.out.println("LOG::CustomerServlet::Initializing CustomerService");
        customerService = CustomerServiceFactory.getCustomerService();
        if (customerService == null) {
            System.err.println("LOG::CustomerServlet::Error - CustomerService is NULL!");
            throw new ServletException("Failed to initialize CustomerService");
        } else {
            System.out.println("LOG::CustomerServlet::CustomerService initialized successfully.");
        }
    }

    // CREATE (POST) - Add a new customer
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("LOG::CustomerServlet::doPost::Started");

            // Get JSON data from request
            String jsonData = JsonUtils.getJsonFromRequest(request);
            if (jsonData == null || jsonData.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");  // Set content type to JSON
                response.getWriter().write("{\"Error\": \"JSON request body is missing.\"}");
                return;
            }

            // Extract values from JSON
            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int userId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "userId"));
            String registrationDate = JsonUtils.extractJsonValue(jsonData, "registrationDate");
            String nic = JsonUtils.extractJsonValue(jsonData, "nic");
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String address = JsonUtils.extractJsonValue(jsonData, "address");
            String email = JsonUtils.extractJsonValue(jsonData, "email");
            String phone = JsonUtils.extractJsonValue(jsonData, "phoneNumber");

            // Validate required fields
            if (name == null || name.isEmpty() || email == null || email.isEmpty() || phone == null || phone.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");  // Set content type to JSON
                response.getWriter().write("{\"Error\": \"Missing required fields (name, email, phoneNumber).\"}");
                return;
            }

            // Create DTO object
            CustomerDTO customerDTO = new CustomerDTO(customerId, userId, name, address, nic, phone, registrationDate, email);

            // Check if customer already exists
            if (customerService.existsById(customerId)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.setContentType("application/json");  // Set content type to JSON
                response.getWriter().write("{\"Error\": \"Customer ID already exists.\"}");
                return;
            }

            // Add customer
            boolean customerAdded = customerService.addCustomer(customerDTO);
            if (customerAdded) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");  // Set content type to JSON
                response.getWriter().write("{\"message\": \"Customer added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");  // Set content type to JSON
                response.getWriter().write("{\"Error\": \"Unable to save customer data.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");  // Set content type to JSON
            response.getWriter().write("{\"Error\": \"An error occurred while processing your request.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String customerIdStr = request.getParameter("customerId");
            String searchTerm = request.getParameter("search");

            if (searchTerm != null && !searchTerm.isEmpty()) {
                // If search parameter is provided, filter customers by name or email
                List<CustomerDTO> customers = customerService.searchCustomers(searchTerm);

                if (!customers.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(customers));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No customers found.\"}");
                }
            } else if (customerIdStr != null && !customerIdStr.isEmpty()) {
                // If a specific customer ID is provided
                int customerId = Integer.parseInt(customerIdStr);
                CustomerDTO customer = customerService.getCustomerById(customerId);

                if (customer != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(customer));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Customer not found.\"}");
                }
            } else {
                // No search or customerId, return all customers
                List<CustomerDTO> customers = customerService.getAllCustomers();
                if (!customers.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(customers));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No customers available.\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving customer details.\"}");
        }
    }


    // UPDATE (PUT) - Modify customer details
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);
            if (jsonData == null || jsonData.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json"); // Set the response content type to JSON
                response.getWriter().write("{\"Error\": \"JSON request body is missing.\"}");
                return;
            }

            int customerId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "customerId"));
            int userId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "userId"));
            String registrationDate = JsonUtils.extractJsonValue(jsonData, "registrationDate");
            String nic = JsonUtils.extractJsonValue(jsonData, "nic");
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String address = JsonUtils.extractJsonValue(jsonData, "address");
            String email = JsonUtils.extractJsonValue(jsonData, "email");
            String phone = JsonUtils.extractJsonValue(jsonData, "phoneNumber");

            CustomerDTO customerDTO = new CustomerDTO(customerId, userId, name, address, nic, phone, registrationDate, email);

            if (customerService.updateCustomer(customerDTO)) {
                response.setStatus(HttpServletResponse.SC_OK); // Success
                response.setContentType("application/json"); // Set the response content type to JSON
                response.getWriter().write("{\"message\": \"Customer updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Internal server error
                response.setContentType("application/json"); // Set the response content type to JSON
                response.getWriter().write("{\"Error\": \"Error updating customer.\"}");
            }
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Not found error
            response.setContentType("application/json"); // Set the response content type to JSON
            response.getWriter().write("{\"Error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Internal server error
            response.setContentType("application/json"); // Set the response content type to JSON
            response.getWriter().write("{\"Error\": \"An error occurred while updating customer.\"}");
        }
    }




    // DELETE (DELETE) - Remove a customer
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String customerIdStr = request.getParameter("customerId");
            if (customerIdStr == null || customerIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json"); // Set response type to JSON
                response.getWriter().write("{\"Error\": \"Missing customerId parameter.\"}");
                return;
            }

            int customerId = Integer.parseInt(customerIdStr);
            boolean isDeleted = customerService.deleteCustomer(customerId);

            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK); // HTTP status 200 OK
                response.setContentType("application/json"); // Set response type to JSON
                response.getWriter().write("{\"message\": \"Customer deleted successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP status 500 Internal Server Error
                response.setContentType("application/json"); // Set response type to JSON
                response.getWriter().write("{\"Error\": \"Error deleting customer.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP status 500 Internal Server Error
            response.setContentType("application/json"); // Set response type to JSON
            response.getWriter().write("{\"Error\": \"An error occurred while deleting customer.\"}");
        }
    }

}
