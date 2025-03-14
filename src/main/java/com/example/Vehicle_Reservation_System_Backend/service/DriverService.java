package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;

import java.util.List;

public interface DriverService {
    boolean addDriver(DriverDTO driverDTO);
    DriverDTO getDriverById(int driverId);
    boolean updateDriver(DriverDTO driverDTO);
    boolean deleteDriver(int driverId);
    List<DriverDTO> getAllDrivers();
}
