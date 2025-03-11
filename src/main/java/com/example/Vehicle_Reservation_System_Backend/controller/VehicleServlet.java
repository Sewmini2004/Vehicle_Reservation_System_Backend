package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.factory.VehicleServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/vehicle")
public class VehicleServlet extends HttpServlet {

    public VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        try {
            vehicleService = VehicleServiceFactory.getVehicleService();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // CREATE (POST) - Add a new vehicle
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            String model = JsonUtils.extractJsonValue(jsonData, "model");
            String availabilityStatus = JsonUtils.extractJsonValue(jsonData, "availabilityStatus");
            String registrationNumber = JsonUtils.extractJsonValue(jsonData, "registrationNumber");
            String fuelType = JsonUtils.extractJsonValue(jsonData, "fuelType");
            String carModel = JsonUtils.extractJsonValue(jsonData, "carModel");
            int seatingCapacity = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "seatingCapacity"));

            // Check for missing required fields
            if (carType == null || model == null || registrationNumber == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required fields.");
                return;
            }

            VehicleDTO vehicle = new VehicleDTO(0, carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity);

            if (vehicleService.addVehicle(vehicle)) {
                response.setStatus(HttpServletResponse.SC_CREATED); // Success - 201
                response.getWriter().write("Vehicle added successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
                response.getWriter().write("Error adding vehicle.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error adding vehicle: " + e.getMessage());
        }
    }

    // READ (GET) - Fetch vehicle details by ID or all vehicles
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String vehicleIdParam = request.getParameter("vehicleId");

            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                // Fetch a single vehicle by ID
                int vehicleId = Integer.parseInt(vehicleIdParam);
                VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);

                if (vehicle != null) {
                    response.setStatus(HttpServletResponse.SC_OK); // Success - 200
                    response.getWriter().write(JsonUtils.convertDtoToJson(vehicle)); // Return vehicle as JSON
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Failure - 404
                    response.getWriter().write("{\"Error\" : \"Vehicle not found.\"}");
                }
            } else {
                // Fetch all vehicles if no ID is provided
                List<VehicleDTO> vehicles = vehicleService.getAllVehicles();

                if (!vehicles.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK); // Success - 200
                    response.getWriter().write(JsonUtils.convertDtoToJson(vehicles)); // Return all vehicles as JSON
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT); // No Content - 204
                    response.getWriter().write("{\"Message\" : \"No vehicles available.\"}");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Failure - 400
            response.getWriter().write("{\"Error\" : \"Invalid vehicle ID format.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving vehicle details.\"}");
        }
    }

    // UPDATE (PUT) - Modify vehicle details
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);
            int vehicleId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "vehicleId"));
            String carType = JsonUtils.extractJsonValue(jsonData, "carType");
            String model = JsonUtils.extractJsonValue(jsonData, "model");
            String availabilityStatus = JsonUtils.extractJsonValue(jsonData, "availabilityStatus");
            String registrationNumber = JsonUtils.extractJsonValue(jsonData, "registrationNumber");
            String fuelType = JsonUtils.extractJsonValue(jsonData, "fuelType");
            String carModel = JsonUtils.extractJsonValue(jsonData, "carModel");
            int seatingCapacity = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "seatingCapacity"));

            VehicleDTO vehicle = new VehicleDTO(vehicleId, carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity);

            if (vehicleService.updateVehicle(vehicle)) {
                response.setStatus(HttpServletResponse.SC_OK); // Success - 200
                response.getWriter().write("Vehicle updated successfully!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
                response.getWriter().write("Error updating vehicle.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
            response.getWriter().write("Error updating vehicle.");
        }
    }

    // DELETE (DELETE) - Remove a vehicle
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));

            if (vehicleService.deleteVehicle(vehicleId)) {
                response.setStatus(HttpServletResponse.SC_OK); // Success - 200
                response.getWriter().write("Vehicle deleted successfully!");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Failure - 404
                response.getWriter().write("Vehicle not found.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
            response.getWriter().write("Error deleting vehicle.");
        }
    }
}
