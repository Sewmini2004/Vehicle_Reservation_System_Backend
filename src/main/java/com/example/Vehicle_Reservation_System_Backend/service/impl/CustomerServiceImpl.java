package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    // Regex for email and NIC validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NIC_REGEX = "^(?:19|20)?\\d{2}[0-9]{10}|[0-9]{9}[x|X|v|V]$";

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) throws SQLException {
        // Validate email and NIC
        if (!isValidEmail(customerDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!isValidNIC(customerDTO.getNic())) {
            throw new IllegalArgumentException("Invalid NIC format.");
        }

        // Create CustomerEntity using the Builder Pattern
        CustomerEntity customerEntity = new CustomerEntity.Builder()
                .customerId(customerDTO.getCustomerId())
                .userId(customerDTO.getUserId())
                .name(customerDTO.getName())
                .address(customerDTO.getAddress())
                .nic(customerDTO.getNic())
                .phoneNumber(customerDTO.getPhoneNumber())
                .registrationDate(customerDTO.getRegistrationDate())
                .email(customerDTO.getEmail())
                .build();

        try {
            return customerDao.saveCustomer(customerEntity);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public CustomerDTO getCustomerById(int customerId) {
        CustomerEntity customerEntity = customerDao.getById(customerId);
        if (customerEntity == null) {
            throw new NotFoundException("Customer not found with ID: " + customerId);
        }
        return CustomerConverter.convertToDTO(customerEntity);
    }

    @Override
    public boolean existsById(int customerId) {
        return customerDao.existsById(customerId);
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) {
        // Validate email and NIC
        if (!isValidEmail(customerDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!isValidNIC(customerDTO.getNic())) {
            throw new IllegalArgumentException("Invalid NIC format.");
        }

        boolean isExists = existsById(customerDTO.getCustomerId());
        if (!isExists) {
            throw new NotFoundException("Customer not found with ID: " + customerDTO.getCustomerId());
        }

        // Create CustomerEntity using the Builder Pattern
        CustomerEntity customerEntity = new CustomerEntity.Builder()
                .customerId(customerDTO.getCustomerId())
                .userId(customerDTO.getUserId())
                .name(customerDTO.getName())
                .address(customerDTO.getAddress())
                .nic(customerDTO.getNic())
                .phoneNumber(customerDTO.getPhoneNumber())
                .registrationDate(customerDTO.getRegistrationDate())
                .email(customerDTO.getEmail())
                .build();

        try {
            return customerDao.updateCustomer(customerEntity);
        } catch (NotFoundException e) {
            throw e;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        boolean deleted = customerDao.deleteCustomer(customerId);
        if (!deleted) {
            throw new NotFoundException("Customer not found with ID: " + customerId);
        }
        return true;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerDao.getAllCustomers();
        return customerEntities.stream()
                .map(CustomerConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Helper method to validate NIC format
    private boolean isValidNIC(String nic) {
        Pattern pattern = Pattern.compile(NIC_REGEX);
        Matcher matcher = pattern.matcher(nic);
        return matcher.matches();
    }

    @Override
    public List<CustomerDTO> searchCustomers(String searchTerm) {
        List<CustomerEntity> customerEntities = customerDao.searchCustomers(searchTerm);
        return customerEntities.stream()
                .map(CustomerConverter::convertToDTO)
                .collect(Collectors.toList());
    }


}
