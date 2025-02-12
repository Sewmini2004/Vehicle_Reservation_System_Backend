package com.example.Vehicle_Reservation_System_Backend.service.impl;

import com.example.Vehicle_Reservation_System_Backend.dao.BookingDao;
import com.example.Vehicle_Reservation_System_Backend.dao.impl.BookingDaoImpl;
import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;
import com.example.Vehicle_Reservation_System_Backend.dto.VehicleDTO;
import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.CustomerEntity;
import com.example.Vehicle_Reservation_System_Backend.entity.VehicleEntity;
import com.example.Vehicle_Reservation_System_Backend.exception.NotFoundException;
import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.utils.BookingConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.CustomerConverter;
import com.example.Vehicle_Reservation_System_Backend.utils.VehicleConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {
    private BookingDao bookingDao = new BookingDaoImpl();

    @Override
    public boolean addBooking(BookingDTO bookingDTO) {
        BookingEntity entity = new BookingEntity(bookingDTO);
        return bookingDao.saveBooking(entity);
    }

    @Override
    public BookingDTO getBookingById(int bookingId) {
        BookingEntity entity = bookingDao.getBookingById(bookingId);
        if (entity == null) {
            throw new NotFoundException("Booking with ID " + bookingId + " not found.");
        }
        return BookingConverter.convertToDTO(entity);
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingEntity> bookingEntities = bookingDao.getAllBookings();
        return bookingEntities.stream().map(BookingConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean updateBooking(BookingDTO bookingDTO) {
        BookingEntity entity = new BookingEntity(bookingDTO);
        return bookingDao.updateBooking(entity);
    }


    @Override
    public boolean deleteBooking(int bookingId) {
        return bookingDao.deleteBooking(bookingId);
    }
}
