package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.factory.VehicleServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
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
                response.getWriter().write("{\"Error\": \"Missing required fields.\"}");
                return;
            }

            VehicleDTO vehicle = new VehicleDTO(0, carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity);

            if (vehicleService.addVehicle(vehicle)) {
                response.setStatus(HttpServletResponse.SC_CREATED); // Success - 201
                response.getWriter().write("{\"message\": \"Vehicle added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
                response.getWriter().write("{\"Error\": \"Error adding vehicle.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\": \"Error adding vehicle: " + e.getMessage() + "\"}");
        }
    }

    // READ (GET) - Fetch vehicle details by ID or all vehicles
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String searchTerm = request.getParameter("search");

            // If search term is provided, filter vehicles by car type, model, or other fields
            List<VehicleDTO> vehicles;
            if (searchTerm != null && !searchTerm.isEmpty()) {
                vehicles = vehicleService.searchVehicles(searchTerm);
            } else {
                // If no search term is provided, fetch all vehicles
                vehicles = vehicleService.getAllVehicles();
            }

            if (!vehicles.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(JsonUtils.convertDtoToJson(vehicles));
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("{\"Message\" : \"No vehicles found.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
                response.getWriter().write("{\"message\": \"Vehicle updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
                response.getWriter().write("{\"Error\": \"Error updating vehicle.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Failure - 500
            response.getWriter().write("{\"Error\": \"Error updating vehicle.\"}");
        }
    }

    // DELETE (DELETE) - Remove a vehicle
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vehicleIdStr = request.getParameter("vehicleId");
        if (vehicleIdStr == null || vehicleIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Missing vehicleId parameter.\"}");
            return;
        }

        int vehicleId = Integer.parseInt(vehicleIdStr);
        boolean isDeleted = vehicleService.deleteVehicle(vehicleId);

        if (isDeleted) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Vehicle deleted successfully!\"}");
        } else {
            // If vehicle couldn't be deleted due to constraints, inform the user.
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"Error\": \"Cannot delete vehicle. It is linked to a booking.\"}");
        }
    }
}
