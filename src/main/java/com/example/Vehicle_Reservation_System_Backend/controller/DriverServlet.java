package com.example.Vehicle_Reservation_System_Backend.controller;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import com.example.Vehicle_Reservation_System_Backend.factory.DriverServiceFactory;
import com.example.Vehicle_Reservation_System_Backend.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/driver")
public class DriverServlet extends HttpServlet {

    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        driverService = DriverServiceFactory.getDriverService();
    }

    //  CREATE (POST) - Add a new driver
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String licenseNumber = JsonUtils.extractJsonValue(jsonData, "licenseNumber");
            String status = JsonUtils.extractJsonValue(jsonData, "status");
            String shiftTiming = JsonUtils.extractJsonValue(jsonData, "shiftTiming");
            String phoneNumber = JsonUtils.extractJsonValue(jsonData, "phoneNumber");
            String salary1 = JsonUtils.extractJsonValue(jsonData, "salary");
            double salary = Double.parseDouble(salary1);
            int experienceYears = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "experienceYears"));

            if (name == null || licenseNumber == null || phoneNumber == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing required fields.");
                return;
            }

            DriverDTO driver = new DriverDTO(0, name, licenseNumber, status, shiftTiming, salary, experienceYears, phoneNumber);

            if (driverService.addDriver(driver)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Driver added successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error adding driver.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error adding driver: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String driverIdParam = request.getParameter("driverId");

            if (driverIdParam != null && !driverIdParam.isEmpty()) {
                // Fetch a single driver by ID
                int driverId = Integer.parseInt(driverIdParam);
                DriverDTO driver = driverService.getDriverById(driverId);

                if (driver != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Driver found: " + driver.getName());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Driver not found.");
                }
            } else {
                // Fetch all drivers if no ID is provided
                List<DriverDTO> drivers = driverService.getAllDrivers();

                if (!drivers.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    StringBuilder responseText = new StringBuilder("Drivers List:\n");
                    for (DriverDTO driver : drivers) {
                        responseText.append("ID: ").append(driver.getDriverId())
                                .append(", Name: ").append(driver.getName()).append("\n");
                    }
                    response.getWriter().write(responseText.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("No drivers available.");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid driver ID format.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving driver details.");
        }
    }

    // UPDATE (PUT) - Modify driver details
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jsonData = JsonUtils.getJsonFromRequest(request);
            int driverId = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "driverId"));
            String name = JsonUtils.extractJsonValue(jsonData, "name");
            String licenseNumber = JsonUtils.extractJsonValue(jsonData, "licenseNumber");
            String status = JsonUtils.extractJsonValue(jsonData, "status");
            String shiftTiming = JsonUtils.extractJsonValue(jsonData, "shiftTiming");
            String salary1 = JsonUtils.extractJsonValue(jsonData, "salary");
            double salary = Double.parseDouble(salary1);
            int experienceYears = Integer.parseInt(JsonUtils.extractJsonValue(jsonData, "experienceYears"));
            String phoneNumber = JsonUtils.extractJsonValue(jsonData, "phoneNumber");

            DriverDTO driver = new DriverDTO(driverId, name, licenseNumber, status, shiftTiming, salary, experienceYears, phoneNumber);

            if (driverService.updateDriver(driver)) {
                response.getWriter().write("Driver updated successfully!");
            } else {
                response.getWriter().write("Error updating driver.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error updating driver.");
        }
    }

    // DELETE (DELETE) - Remove a driver
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int driverId = Integer.parseInt(request.getParameter("driverId"));

            if (driverService.deleteDriver(driverId)) {
                response.getWriter().write("Driver deleted successfully!");
            } else {
                response.getWriter().write("Error deleting driver.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error deleting driver.");
        }
    }
}
