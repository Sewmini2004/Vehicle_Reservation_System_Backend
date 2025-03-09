package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.VehicleDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.VehicleDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.VehicleService;
import com.example.Vehicle_Reservation_System_Backend.utils.VehicleConverter;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleServiceImpl implements VehicleService {

    private VehicleDao vehicleDao;

    public VehicleServiceImpl(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    @Override
    public boolean addVehicle(VehicleDTO vehicleDTO) {
        VehicleEntity vehicleEntity = VehicleConverter.convertToEntity(vehicleDTO);
        return vehicleDao.saveVehicle(vehicleEntity);
    }

    @Override
    public VehicleDTO getVehicleById(int vehicleId) {
        VehicleEntity vehicleEntity = vehicleDao.getById(vehicleId);
        if (vehicleEntity == null) {
            throw new NotFoundException("Vehicle with ID " + vehicleId + " not found.");
        }
        return VehicleConverter.convertToDTO(vehicleEntity);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<VehicleEntity> vehicleEntities = vehicleDao.getAllVehicles();
        return vehicleEntities.stream().map(VehicleConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean updateVehicle(VehicleDTO vehicleDTO) {
        VehicleEntity vehicleEntity = VehicleConverter.convertToEntity(vehicleDTO);
        return vehicleDao.updateVehicle(vehicleEntity);
    }

    @Override
    public boolean deleteVehicle(int vehicleId) {
        return vehicleDao.deleteVehicle(vehicleId);
    }


    @Override
    public boolean existsById(int vehicleId) {
        VehicleEntity vehicleEntity = vehicleDao.getById(vehicleId);
        return vehicleEntity != null;
    }

}
