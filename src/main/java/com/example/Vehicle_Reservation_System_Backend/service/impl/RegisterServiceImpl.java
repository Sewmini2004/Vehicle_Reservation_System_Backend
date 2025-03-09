package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.RegisterDao;
import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.RegisterEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.AlreadyException;
import com.example.Vehicle_Reservation_System_Backend.service.RegisterService;
import com.example.Vehicle_Reservation_System_Backend.utils.RegisterConverter;
import static com.example.Vehicle_Reservation_System_Backend.utils.RegisterConverter.convertToDTO;
import static com.example.Vehicle_Reservation_System_Backend.utils.RegisterConverter.convertToEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class RegisterServiceImpl implements RegisterService {
    private RegisterDao registerDao;

    public RegisterServiceImpl(RegisterDao registerDao) {
        this.registerDao = registerDao;
    }

    @Override
    public boolean addUser(RegisterDTO registerDTO) throws SQLException {
        if (existsByUsername(registerDTO.getUsername())) {
            throw new AlreadyException("Username already exists.");
        }
        RegisterEntity registerEntity = convertToEntity(registerDTO);
        return registerDao.saveUser(registerEntity);
    }

    @Override
    public RegisterDTO getUserById(int userId) {
        return convertToDTO(registerDao.getById(userId));
    }

    @Override
    public boolean existsByUsername(String username) {
        return registerDao.existsByUsername(username);
    }

    @Override
    public boolean updateUser(RegisterDTO registerDTO) {
        RegisterEntity registerEntity = convertToEntity(registerDTO);
        return registerDao.updateUser(registerEntity);
    }

    @Override
    public boolean deleteUser(int userId) {
        return registerDao.deleteUser(userId);
    }

    @Override
    public List<RegisterDTO> getAllUsers() {
        return registerDao.getAllUsers().stream()
                .map(RegisterConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
