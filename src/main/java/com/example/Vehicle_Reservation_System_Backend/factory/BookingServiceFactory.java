package com.example.Vehicle_Reservation_System_Backend.factory;

import com.example.Vehicle_Reservation_System_Backend.service.BookingService;
import com.example.Vehicle_Reservation_System_Backend.service.impl.BookingServiceImpl;

public class BookingServiceFactory {
    // Singleton instance of BookingService
    private static BookingService bookingServiceInstance;

    public static BookingService getBookingService() {
        if (bookingServiceInstance == null) {
            bookingServiceInstance = new BookingServiceImpl();
        }
        return bookingServiceInstance;
    }
}
