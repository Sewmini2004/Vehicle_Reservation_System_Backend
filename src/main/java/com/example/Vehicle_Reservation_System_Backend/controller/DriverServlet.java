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

    public DriverService driverService;

    @Override
    public void init() throws ServletException {
        driverService = DriverServiceFactory.getDriverService();
    }

    // CREATE (POST) - Add a new driver
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
                response.getWriter().write("{\"Error\" : \"Missing required fields.\"}");
                return;
            }

            DriverDTO driver = new DriverDTO.Builder()
                    .driverId(0)
                    .name(name)
                    .licenseNumber(licenseNumber)
                    .status(status)
                    .shiftTiming(shiftTiming)
                    .salary(salary)
                    .experienceYears(experienceYears)
                    .phoneNumber(phoneNumber)
                    .build();
            if (driverService.addDriver(driver)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"Message\" : \"Driver added successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"Error\" : \"Error adding driver.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"Error adding driver: " + e.getMessage() + "\"}");
        }
    }

    // READ (GET) - Get driver(s)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String driverIdParam = request.getParameter("driverId");

            if (driverIdParam != null && !driverIdParam.isEmpty()) {
                int driverId = Integer.parseInt(driverIdParam);
                DriverDTO driver = driverService.getDriverById(driverId);

                if (driver != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(driver));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"Error\" : \"Driver not found.\"}");
                }
            } else {
                List<DriverDTO> drivers = driverService.getAllDrivers();

                if (!drivers.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(JsonUtils.convertDtoToJson(drivers));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    response.getWriter().write("{\"Message\" : \"No drivers available.\"}");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"Error\" : \"Invalid driver ID format.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"An error occurred while retrieving driver details.\"}");
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
            DriverDTO driver = new DriverDTO.Builder()
                    .driverId(driverId)
                    .name(name)
                    .licenseNumber(licenseNumber)
                    .status(status)
                    .shiftTiming(shiftTiming)
                    .salary(salary)
                    .experienceYears(experienceYears)
                    .phoneNumber(phoneNumber)
                    .build();

            if (driverService.updateDriver(driver)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"Message\" : \"Driver updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"Error\" : \"Error updating driver.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"Error updating driver: " + e.getMessage() + "\"}");
        }
    }

    // DELETE (DELETE) - Remove a driver
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int driverId = Integer.parseInt(request.getParameter("driverId"));

            if (driverService.deleteDriver(driverId)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"Message\" : \"Driver deleted successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"Error\" : \"Driver not found.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"Error\" : \"Error deleting driver: " + e.getMessage() + "\"}");
        }
    }
}
