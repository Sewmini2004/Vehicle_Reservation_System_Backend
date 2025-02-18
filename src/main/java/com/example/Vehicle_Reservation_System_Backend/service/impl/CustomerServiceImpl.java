package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.CustomerDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.VehicleConverter;

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

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
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

        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        try {
            return customerDao.saveCustomer(customerEntity);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public CustomerDTO getCustomerById(int customerId) {
        CustomerEntity customerEntity = customerDao.getById(customerId);
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
        Boolean isExists = existsById(customerDTO.getCustomerId());
        if (!isExists) {
            throw new NotFoundException("Customer is not found");
        }
        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        try {
            return customerDao.updateCustomer(customerEntity);
        } catch (NotFoundException e) {
            throw e;
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

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> customerEntities = customerDao.getAllCustomers();
        return customerEntities.stream().map(CustomerConverter::convertToDTO).collect(Collectors.toList());
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
