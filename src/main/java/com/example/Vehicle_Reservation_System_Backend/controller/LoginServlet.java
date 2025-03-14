package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.factory.RegisterServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.RegisterService;
import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private RegisterService registerService;

    @Override
    public void init() throws ServletException {
        registerService = RegisterServiceFactory.getRegisterService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get JSON data from request
            String jsonData = JsonUtils.getJsonFromRequest(request);
            if (jsonData == null || jsonData.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Error\": \"Missing JSON request body.\"}");
                return;
            }

            // Extract username and password from request
            String username = JsonUtils.extractJsonValue(jsonData, "username");
            String password = JsonUtils.extractJsonValue(jsonData, "password");

            // Validate input
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Error\": \"Username and password are required.\"}");
                return;
            }

            // Check if the user exists
            RegisterDTO user = registerService.getUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"Message\": \"Login successful.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"Error\": \"Invalid username or password.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\": \"An error occurred while processing the request.\"}");
        }
    }
}
