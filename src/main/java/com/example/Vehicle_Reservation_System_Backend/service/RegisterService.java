package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.RegisterDTO;
import java.sql.SQLException;
import java.util.List;

public interface RegisterService {
    boolean addUser(RegisterDTO registerDTO) throws SQLException;
    RegisterDTO getUserById(int userId);
    boolean existsByUsername(String username);
    boolean updateUser(RegisterDTO registerDTO);
    boolean deleteUser(int userId);
    List<RegisterDTO> getAllUsers();
    RegisterDTO getUserByUsername(String username);

}
