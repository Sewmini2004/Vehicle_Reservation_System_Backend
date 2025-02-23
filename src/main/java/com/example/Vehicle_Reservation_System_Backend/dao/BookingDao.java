package com.example.Vehicle_Reservation_System_Backend.dao;

import com.example.Vehicle_Reservation_System_Backend.entity.BookingEntity;

import java.util.List;

public interface BookingDao {
    int saveBooking(BookingEntity booking);
    BookingEntity getBookingById(int bookingId);
    List<BookingEntity> getAllBookings();
    boolean updateBooking(BookingEntity booking);
    boolean deleteBooking(int bookingId);
}
