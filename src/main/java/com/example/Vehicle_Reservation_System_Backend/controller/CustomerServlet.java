package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
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
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            CustomerDTO customerDTO = new CustomerDTO(0, 0, name, "", "", phone, "", email);

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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            CustomerDTO customer = customerService.getCustomerById(customerId);

            if (customer != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Customer found: " + customer.getName());
            }
        } catch (NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing your request.");
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
