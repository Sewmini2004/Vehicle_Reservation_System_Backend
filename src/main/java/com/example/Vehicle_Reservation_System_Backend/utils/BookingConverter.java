package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;

public class BookingConverter {

    // Convert BookingEntity to BookingDTO
    public static BookingDTO convertToDTO(BookingEntity entity) {
        return new BookingDTO(
                entity.getBookingId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getDriverId(),
                entity.getPickupLocation(),
                entity.getDropLocation(),
                entity.getBookingDate(),
                entity.getCarType(),
                entity.getTotalBill(),
                entity.getCancelStatus(),
                entity.getDistance(),
                entity.getCustomerName(),
                entity.getDriverName(),
                entity.getVehicleModel(),
                entity.getVehicleRegistrationNumber()
        );
    }

    // Convert BookingDTO to BookingEntity
    public static BookingEntity convertToEntity(BookingDTO dto) {
        return new BookingEntity(
                dto.getBookingId(),
                dto.getCustomerId(),
                dto.getVehicleId(),
                dto.getDriverId(),
                dto.getPickupLocation(),
                dto.getDropLocation(),
                dto.getBookingDate(),
                dto.getCarType(),
                dto.getTotalBill(),
                dto.getDistance(),
                dto.getCancelStatus()
        );
    }
}
