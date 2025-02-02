package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;

public class CustomerConverter {

    public static CustomerDTO convertToDTO(CustomerEntity customerEntity) {
        return new CustomerDTO(
                customerEntity.getCustomerId(),
                customerEntity.getUserId(),
                customerEntity.getName(),
                customerEntity.getAddress(),
                customerEntity.getNic(),
                customerEntity.getPhoneNumber(),
                customerEntity.getRegistrationDate(),
                customerEntity.getEmail()
        );
    }

    public static CustomerEntity convertToEntity(CustomerDTO customerDTO) {
        return new CustomerEntity(
                customerDTO.getCustomerId(),
                customerDTO.getUserId(),
                customerDTO.getName(),
                customerDTO.getAddress(),
                customerDTO.getNic(),
                customerDTO.getPhoneNumber(),
                customerDTO.getRegistrationDate(),
                customerDTO.getEmail()
        );
    }
}
