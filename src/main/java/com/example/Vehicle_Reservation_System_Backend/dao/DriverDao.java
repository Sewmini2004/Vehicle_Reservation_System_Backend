package com.example.Vehicle_Reservation_System_Backend.dao;

import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.DriverEntity;

import java.util.List;

public interface DriverDao {
    boolean saveDriver(DriverEntity driverEntity);
    DriverEntity getById(int driverId);
    List<DriverEntity> getAllDrivers();
    boolean updateDriver(DriverEntity driverEntity);
    boolean deleteDriver(int driverId);
    boolean existsById(int id);
}
