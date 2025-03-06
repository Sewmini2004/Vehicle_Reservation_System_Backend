package com.example.Vehicle_Reservation_System_Backend.converter;

import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.RegisterEntity;

public class RegisterConverter {

    public static RegisterDTO convertToDTO(RegisterEntity registerEntity) {
        return new RegisterDTO(
                registerEntity.getUserId(),
                registerEntity.getUsername(),
                registerEntity.getPassword(),
                registerEntity.getFirstName(),
                registerEntity.getLastName(),
                registerEntity.getEmail(),
                registerEntity.getCreatedAt()
        );
    }

    public static RegisterEntity convertToEntity(RegisterDTO registerDTO) {
        return new RegisterEntity(
                registerDTO.getUserId(),
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getFirstName(),
                registerDTO.getLastName(),
                registerDTO.getEmail(),
                registerDTO.getCreatedAt()
        );
    }
}
