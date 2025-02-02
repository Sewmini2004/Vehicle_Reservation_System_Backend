package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.factory.CustomerServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        customerService = CustomerServiceFactory.getCustomerService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic");
        String phoneNumber = request.getParameter("phone");
        String registrationDate = request.getParameter("registrationDate");
        String email = request.getParameter("email");
        int userId = Integer.parseInt(request.getParameter("userId"));

        CustomerDTO customer = new CustomerDTO(0, userId, name, address, nic, phoneNumber, registrationDate, email);

        if (customerService.addCustomer(customer)) {
            response.getWriter().write("Customer added successfully!");
        } else {
            response.getWriter().write("Error adding customer.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        CustomerDTO customer = customerService.getCustomerById(customerId);

        if (customer != null) {
            response.getWriter().write("Customer Details: " + customer.toString());
        } else {
            response.getWriter().write("Customer not found.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String nic = request.getParameter("nic");
        String phoneNumber = request.getParameter("phone");
        String registrationDate = request.getParameter("registrationDate");
        String email = request.getParameter("email");

        CustomerDTO customer = new CustomerDTO(customerId, 0, name, address, nic, phoneNumber, registrationDate, email);

        if (customerService.updateCustomer(customer)) {
            response.getWriter().write("Customer updated successfully!");
        } else {
            response.getWriter().write("Error updating customer.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));

        if (customerService.deleteCustomer(customerId)) {
            response.getWriter().write("Customer deleted successfully!");
        } else {
            response.getWriter().write("Error deleting customer.");
        }
    }
}
