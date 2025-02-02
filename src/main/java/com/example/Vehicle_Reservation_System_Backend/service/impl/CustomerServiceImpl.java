package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.CustomerDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.CustomerDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.service.CustomerService;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao;

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDaoImpl();
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        return customerDao.saveCustomer(customerEntity);
    }

    @Override
    public CustomerDTO getCustomerById(int customerId) {
        CustomerEntity customerEntity = customerDao.getById(customerId);
        return CustomerConverter.convertToDTO(customerEntity);
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = CustomerConverter.convertToEntity(customerDTO);
        return customerDao.updateCustomer(customerEntity);
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        return customerDao.deleteCustomer(customerId);
    }
}
