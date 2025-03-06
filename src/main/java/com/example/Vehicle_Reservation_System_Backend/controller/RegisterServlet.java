package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.factory.RegisterServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.RegisterService;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private RegisterService registerService;

    @Override
    public void init() throws ServletException {
        System.out.println("LOG::RegisterServlet::Initializing RegisterService");
        registerService = RegisterServiceFactory.getRegisterService();

        if (registerService == null) {
            System.err.println("LOG::RegisterServlet::Error - RegisterService is NULL!");
            throw new ServletException("Failed to initialize RegisterService");
        } else {
            System.out.println("LOG::RegisterServlet::RegisterService initialized successfully.");
        }
    }

    // CREATE (POST) - Register a new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("LOG::RegisterServlet::doPost::Started");

            // Get JSON data from request
            String jsonData = JsonUtils.getJsonFromRequest(request);
            if (jsonData == null || jsonData.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Error\" : \"JSON request body is missing.\"}");
                return;
            }

            // Extract values from JSON
            int userId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "userId"));
            String username = JsonUtils.extractJsonValue(jsonData, "username");
            String password = JsonUtils.extractJsonValue(jsonData, "password");
            String firstName = JsonUtils.extractJsonValue(jsonData, "firstName");
            String lastName = JsonUtils.extractJsonValue(jsonData, "lastName");
            String email = JsonUtils.extractJsonValue(jsonData, "email");
            String createdAt = JsonUtils.extractJsonValue(jsonData, "createdAt");

            // Validate required fields
            if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"Error\" : \"Missing required fields (username, password, email).\"}");
                return;
            }

            // Create DTO object
            RegisterDTO registerDTO = new RegisterDTO(userId, username, password, firstName, lastName, email, createdAt);

            // Check if user already exists
            if (registerService.existsByUsername(username)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"Error\" : \"Username already exists.\"}");
                return;
            }

            // Register user
            if (registerService.addUser(registerDTO)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"Message\" : \"User registered successfully.\"}");
            }

        } catch (IllegalArgumentException | AlreadyException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Error\" : \" " + e.getMessage() + " \"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \" An error occurred while processing your request. \"}");
        }
    }

    // RETRIEVE (GET) - Get user details
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");

            // Check if a specific user ID is provided
            if (userIdStr != null && !userIdStr.isEmpty()) {
                int userId = Integer.parseInt(userIdStr);
                RegisterDTO user = registerService.getUserById(userId);

                if (user != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(user));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"User not found.\"}");
                }
            } else {
                // If no user ID is provided, fetch all users
                List<RegisterDTO> users = registerService.getAllUsers();

                if (!users.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(users));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No users available.\"}");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Error\" : \"Invalid userId format.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving user details.\"}");
        }
    }
}
