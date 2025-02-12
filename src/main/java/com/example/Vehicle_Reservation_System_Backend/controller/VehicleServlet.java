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
import java.util.List;

@WebServlet("/vehicle")
public class VehicleServlet extends HttpServlet {

    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        vehicleService = VehicleServiceFactory.getVehicleService();
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

            if (carType == null || model == null || registrationNumber == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required fields.");
                return;
            }

            VehicleDTO vehicle = new VehicleDTO(0, carType, model, availabilityStatus, registrationNumber, fuelType, carModel, seatingCapacity);

            if (vehicleService.addVehicle(vehicle)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Vehicle added successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error adding vehicle.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error adding vehicle: " + e.getMessage());
        }
    }

    // READ (GET) - Fetch a specific vehicle by ID or list all vehicles
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String vehicleIdParam = request.getParameter("vehicleId");

            if (vehicleIdParam != null && !vehicleIdParam.isEmpty()) {
                // Fetch a single vehicle by ID
                int vehicleId = Integer.parseInt(vehicleIdParam);
                VehicleDTO vehicle = vehicleService.getVehicleById(vehicleId);

                if (vehicle != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Vehicle found: " + vehicle.getModel() + " (" + vehicle.getRegistrationNumber() + ")");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Vehicle not found.");
                }
            } else {
                // Fetch all vehicles if no ID is provided
                List<VehicleDTO> vehicles = vehicleService.getAllVehicles();

                if (!vehicles.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    StringBuilder responseText = new StringBuilder("Vehicle List:\n");
                    for (VehicleDTO vehicle : vehicles) {
                        responseText.append("ID: ").append(vehicle.getVehicleId())
                                .append(", Model: ").append(vehicle.getModel())
                                .append(", Registration: ").append(vehicle.getRegistrationNumber()).append("\n");
                    }
                    response.getWriter().write(responseText.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("No vehicles available.");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid vehicle ID format.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving vehicle details.");
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
                response.getWriter().write("Vehicle updated successfully!");
            } else {
                response.getWriter().write("Error updating vehicle.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating vehicle.");
        }
    }

    // DELETE (DELETE) - Remove a vehicle
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));

            if (vehicleService.deleteVehicle(vehicleId)) {
                response.getWriter().write("Vehicle deleted successfully!");
            } else {
                response.getWriter().write("Error deleting vehicle.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error deleting vehicle.");
        }
    }
}
