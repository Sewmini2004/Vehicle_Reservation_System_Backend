package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.BillingDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BillingEntity;
import java.sql.Timestamp;

public class BillingConverter {

    // Convert BillingEntity to BillingDTO
    public static BillingDTO convertToDTO(BillingEntity entity) {
        return new BillingDTO(
                entity.getBillId(),
                entity.getBookingId(),
                entity.getTotalAmount(),
                entity.getDiscountAmount(),
                entity.getTaxAmount(),
                entity.getFinalAmount(),
                entity.getPaymentMethod(),
                entity.getPaymentStatus(),
                entity.getCreatedAt()
        );
    }

    // Convert BillingDTO to BillingEntity
    public static BillingEntity convertToEntity(BillingDTO dto) {
        return new BillingEntity(
                dto.getBillId(),
                dto.getBookingId(),
                dto.getTotalAmount(),
                dto.getDiscountAmount(),
                dto.getTaxAmount(),
                dto.getFinalAmount(),
                dto.getPaymentMethod(),
                dto.getPaymentStatus(),
                dto.getCreatedAt()
        );
    }
}
