package com.example.Vehicle_Reservation_System_Backend.dao;

import com.example.Vehicle_Reservation_System_Backend.entity.RegisterEntity;
import java.sql.SQLException;
import java.util.List;

public interface RegisterDao {
    boolean saveUser(RegisterEntity registerEntity) throws SQLException;
    boolean updateUser(RegisterEntity registerEntity);
    boolean deleteUser(int userId);
    List<RegisterEntity> getAllUsers();
    RegisterEntity getById(int userId);
    boolean existsByUsername(String username);
}
