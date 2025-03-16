package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.DriverDao;
import com.example.Vehicle_Reservation_System_Backend.dto.DriverDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;
import com.example.Vehicle_Reservation_System_Backend.service.DriverService;
import com.example.Vehicle_Reservation_System_Backend.utils.DriverConverter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DriverServiceImpl implements DriverService {

    private DriverDao driverDao;

    private static final String LICENSE_NUMBER_REGEX = "^[A-Z0-9-]{6,12}$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";

    public DriverServiceImpl(DriverDao driverDao) {
        this.driverDao = driverDao;
    }

    @Override
    public boolean addDriver(DriverDTO driverDTO) {
        if (!isValidLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Invalid license number format.");
        }

        if (!isValidPhoneNumber(driverDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        DriverEntity driverEntity = DriverConverter.convertToEntity(driverDTO);
        return driverDao.saveDriver(driverEntity);
    }

    @Override
    public DriverDTO getDriverById(int driverId) {
        DriverEntity driverEntity = driverDao.getById(driverId);
        return DriverConverter.convertToDTO(driverEntity);
    }

    @Override
    public boolean updateDriver(DriverDTO driverDTO) {
        if (!isValidLicenseNumber(driverDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Invalid license number format.");
        }

        if (!isValidPhoneNumber(driverDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }

        DriverEntity driverEntity = DriverConverter.convertToEntity(driverDTO);
        return driverDao.updateDriver(driverEntity);
    }

    @Override
    public boolean deleteDriver(int driverId) {
        return driverDao.deleteDriver(driverId);
    }

    @Override
    public List<DriverDTO> getAllDrivers() {
        return driverDao.getAllDrivers().stream().map(DriverConverter::convertToDTO).collect(Collectors.toList());
    }

    private boolean isValidLicenseNumber(String licenseNumber) {
        Pattern pattern = Pattern.compile(LICENSE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(licenseNumber);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
