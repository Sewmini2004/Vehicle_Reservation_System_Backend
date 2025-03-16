package com.example.Vehicle_Reservation_System_Backend.service;

import com.example.Vehicle_Reservation_System_Backend.dto.BookingDTO;

import java.sql.SQLException;
import java.util.List;

public interface BookingService {
    boolean addBooking(BookingDTO booking) throws SQLException;
    BookingDTO getBookingById(int bookingId) throws SQLException;
    List<BookingDTO> getAllBookings() throws SQLException;
    boolean updateBooking(BookingDTO booking);
    boolean deleteBooking(int bookingId) throws SQLException;
    public List<BookingDTO> searchBookings(String searchTerm);
}
