package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;

public interface DriverService {
    boolean addDriver(DriverDTO driverDTO);
    DriverDTO getDriverById(int driverId);
    boolean updateDriver(DriverDTO driverDTO);
    boolean deleteDriver(int driverId);
}
