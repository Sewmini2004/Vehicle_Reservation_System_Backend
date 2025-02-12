package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;

public class VehicleConverter {

    public static VehicleDTO convertToDTO(VehicleEntity vehicleEntity) {
        return new VehicleDTO(
                vehicleEntity.getVehicleId(),
                vehicleEntity.getCarType(),
                vehicleEntity.getModel(),
                vehicleEntity.getAvailabilityStatus(),
                vehicleEntity.getRegistrationNumber(),
                vehicleEntity.getFuelType(),
                vehicleEntity.getCarModel(),
                vehicleEntity.getSeatingCapacity()
        );
    }

    public static VehicleEntity convertToEntity(VehicleDTO vehicleDTO) {
        return new VehicleEntity(
                vehicleDTO.getVehicleId(),
                vehicleDTO.getCarType(),
                vehicleDTO.getModel(),
                vehicleDTO.getAvailabilityStatus(),
                vehicleDTO.getRegistrationNumber(),
                vehicleDTO.getFuelType(),
                vehicleDTO.getCarModel(),
                vehicleDTO.getSeatingCapacity()
        );
    }
}
