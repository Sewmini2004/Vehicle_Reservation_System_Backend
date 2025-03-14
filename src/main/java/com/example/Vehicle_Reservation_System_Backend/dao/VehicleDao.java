package com.example.Vehicle_Reservation_System_Backend.dao;

import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;
import java.util.List;

public interface VehicleDao {
    boolean saveVehicle(VehicleEntity vehicleEntity);
    VehicleEntity getById(int vehicleId);
    List<VehicleEntity> getAllVehicles();
    boolean updateVehicle(VehicleEntity vehicleEntity);
    boolean deleteVehicle(int vehicleId);
    boolean existsById(int id);

}
