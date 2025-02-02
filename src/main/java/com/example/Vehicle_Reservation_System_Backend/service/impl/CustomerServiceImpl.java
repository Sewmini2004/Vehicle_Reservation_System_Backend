package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.CustomerDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    // Regex for email and NIC validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NIC_REGEX = "\\d{10}"; // Assuming NIC is a 10-digit number

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) {
        // Validate email and NIC
        if (!isValidEmail(customerDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!isValidNIC(customerDTO.getNic())) {
            throw new IllegalArgumentException("Invalid NIC format.");
        }

        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        try {
            return customerDao.saveCustomer(customerEntity);
        } catch (AlreadyException e) {
            throw e;  // Propagate the exception to the controller
        }
    }

    @Override
    public CustomerDTO getCustomerById(int customerId) {
        CustomerEntity customerEntity = customerDao.getById(customerId);
        return CustomerConverter.convertToDTO(customerEntity);
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

        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        try {
            return customerDao.updateCustomer(customerEntity);
        } catch (NotFoundException e) {
            throw e;  // Propagate the exception to the controller
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        try {
            return customerDao.deleteCustomer(customerId);
        } catch (NotFoundException e) {
            throw e;  // Propagate the exception to the controller
        }
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
}
