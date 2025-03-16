package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {
    boolean addVehicle(VehicleDTO vehicleDTO);
    VehicleDTO getVehicleById(int vehicleId);
    List<VehicleDTO> getAllVehicles();
    boolean updateVehicle(VehicleDTO vehicleDTO);
    boolean deleteVehicle(int vehicleId);
    boolean existsById(int vehicleId);
    public List<VehicleDTO> searchVehicles(String searchTerm);
}
