package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.dao.RegisterDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.RegisterDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.service.RegisterService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.RegisterServiceImpl;
import com.example.Vehicle_Reservation_System_Backend.utils.DBConnection;

import java.sql.Connection;

public class RegisterServiceFactory {
    private static RegisterService registerServiceInstance;

    public static RegisterService getRegisterService() {
        if (registerServiceInstance == null) {
            Connection connection = DBConnection.getInstance().getConnection();
            RegisterDao registerDao = new RegisterDaoImpl(connection);
            registerServiceInstance = new RegisterServiceImpl(registerDao);
        }
        return registerServiceInstance;
    }
}
