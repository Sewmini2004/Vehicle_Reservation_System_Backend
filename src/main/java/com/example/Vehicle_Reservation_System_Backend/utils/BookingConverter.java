package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;

public class BookingConverter {

    // Convert BookingEntity to BookingDTO
    public static BookingDTO convertToDTO(BookingEntity entity) {
        return new BookingDTO.Builder()
                .bookingId(entity.getBookingId())
                .customerId(entity.getCustomerId())
                .vehicleId(entity.getVehicleId())
                .driverId(entity.getDriverId())
                .pickupLocation(entity.getPickupLocation())
                .dropLocation(entity.getDropLocation())
                .bookingDate(entity.getBookingDate())
                .carType(entity.getCarType())
                .totalBill(entity.getTotalBill())
                .cancelStatus(entity.getCancelStatus())
                .distance(entity.getDistance())
                .customerName(entity.getCustomerName())
                .driverName(entity.getDriverName())
                .vehicleModel(entity.getVehicleModel())
                .vehicleRegistrationNumber(entity.getVehicleRegistrationNumber())
                .build();
    }

    // Convert BookingDTO to BookingEntity
    public static BookingEntity convertToEntity(BookingDTO dto) {
        return new BookingEntity.Builder()
                .bookingId(dto.getBookingId())
                .customerId(dto.getCustomerId())
                .vehicleId(dto.getVehicleId())
                .driverId(dto.getDriverId())
                .pickupLocation(dto.getPickupLocation())
                .dropLocation(dto.getDropLocation())
                .bookingDate(dto.getBookingDate())
                .carType(dto.getCarType())
                .totalBill(dto.getTotalBill())
                .distance(dto.getDistance())
                .cancelStatus(dto.getCancelStatus())
                .customerName(dto.getCustomerName())
                .driverName(dto.getDriverName())
                .vehicleModel(dto.getVehicleModel())
                .vehicleRegistrationNumber(dto.getVehicleRegistrationNumber())
                .build();
    }
}
