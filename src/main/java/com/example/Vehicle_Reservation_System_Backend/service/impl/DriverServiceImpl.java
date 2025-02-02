package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.DriverDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.DriverDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import com.example.Vehicle_Reservation_System_Backend.utils.DriverConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverServiceImpl implements DriverService {

    private DriverDao driverDao;

    // Regex for license number and phone number validation
    private static final String LICENSE_NUMBER_REGEX = "^[A-Z0-9-]{6,12}$";  // Example for license number format (modify if needed)
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";  // Assuming 10-digit phone number

    public DriverServiceImpl() {
        this.driverDao = new DriverDaoImpl();
    }

    @Override
    public boolean addDriver(DriverDTO driverDTO) {
        // Validate license number and phone number
        if (!isValidLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Invalid license number format.");
        }

        if (!isValidPhoneNumber(driverDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        DriverEntity driverEntity = DriverConverter.convertToEntity(driverDTO);
        try {
            return driverDao.saveDriver(driverEntity);
        } catch (AlreadyException e) {
            throw e;  // Propagate the exception to the controller
        }
    }

    @Override
    public DriverDTO getDriverById(int driverId) {
        DriverEntity driverEntity = driverDao.getById(driverId);
        return DriverConverter.convertToDTO(driverEntity);
    }

    @Override
    public boolean updateDriver(DriverDTO driverDTO) {
        // Validate license number and phone number
        if (!isValidLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Invalid license number format.");
        }

        if (!isValidPhoneNumber(driverDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        DriverEntity driverEntity = DriverConverter.convertToEntity(driverDTO);
        try {
            return driverDao.updateDriver(driverEntity);
        } catch (NotFoundException e) {
            throw e;  // Propagate the exception to the controller
        }
    }

    @Override
    public boolean deleteDriver(int driverId) {
        return driverDao.deleteDriver(driverId);
    }

    // Helper method to validate license number format
    private boolean isValidLicenseNumber(String licenseNumber) {
        Pattern pattern = Pattern.compile(LICENSE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(licenseNumber);
        return matcher.matches();
    }

    // Helper method to validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
