package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;

public class DriverConverter {

    public static DriverDTO convertToDTO(DriverEntity driverEntity) {
        return new DriverDTO(
                driverEntity.getDriverId(),
                driverEntity.getVehicleId(),
                driverEntity.getName(),
                driverEntity.getLicenseNumber(),
                driverEntity.getStatus(),
                driverEntity.getShiftTiming(),
                driverEntity.getSalary(),
                driverEntity.getExperienceYears(),
                driverEntity.getPhoneNumber()
        );
    }

    public static DriverEntity convertToEntity(DriverDTO driverDTO) {
        return new DriverEntity(
                driverDTO.getDriverId(),
                driverDTO.getVehicleId(),
                driverDTO.getName(),
                driverDTO.getLicenseNumber(),
                driverDTO.getStatus(),
                driverDTO.getShiftTiming(),
                driverDTO.getSalary(),
                driverDTO.getExperienceYears(),
                driverDTO.getPhoneNumber()
        );
    }
}
