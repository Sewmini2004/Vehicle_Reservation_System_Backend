package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;

public class DriverConverter {

    public static DriverDTO convertToDTO(DriverEntity driverEntity) {
        return new DriverDTO.Builder()
                .driverId(driverEntity.getDriverId())
                .name(driverEntity.getName())
                .licenseNumber(driverEntity.getLicenseNumber())
                .status(driverEntity.getStatus())
                .shiftTiming(driverEntity.getShiftTiming())
                .salary(driverEntity.getSalary())
                .experienceYears(driverEntity.getExperienceYears())
                .phoneNumber(driverEntity.getPhoneNumber())
                .build();
    }

    public static DriverEntity convertToEntity(DriverDTO driverDTO) {
        return new DriverEntity.Builder()
                .driverId(driverDTO.getDriverId())
                .name(driverDTO.getName())
                .licenseNumber(driverDTO.getLicenseNumber())
                .status(driverDTO.getStatus())
                .shiftTiming(driverDTO.getShiftTiming())
                .salary(driverDTO.getSalary())
                .experienceYears(driverDTO.getExperienceYears())
                .phoneNumber(driverDTO.getPhoneNumber())
                .build();
    }
}
