package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;

public class CustomerConverter {

    // Convert CustomerEntity to CustomerDTO using Builder Pattern
    public static CustomerDTO convertToDTO(CustomerEntity customerEntity) {
        return new CustomerDTO.Builder()
                .customerId(customerEntity.getCustomerId())
                .userId(customerEntity.getUserId())
                .name(customerEntity.getName())
                .address(customerEntity.getAddress())
                .nic(customerEntity.getNic())
                .phoneNumber(customerEntity.getPhoneNumber())
                .registrationDate(customerEntity.getRegistrationDate())
                .email(customerEntity.getEmail())
                .build();
    }

    // Convert CustomerDTO to CustomerEntity using Builder Pattern
    public static CustomerEntity convertToEntity(CustomerDTO customerDTO) {
        return new CustomerEntity.Builder()
                .customerId(customerDTO.getCustomerId())
                .userId(customerDTO.getUserId())
                .name(customerDTO.getName())
                .address(customerDTO.getAddress())
                .nic(customerDTO.getNic())
                .phoneNumber(customerDTO.getPhoneNumber())
                .registrationDate(customerDTO.getRegistrationDate())
                .email(customerDTO.getEmail())
                .build();
    }
}
