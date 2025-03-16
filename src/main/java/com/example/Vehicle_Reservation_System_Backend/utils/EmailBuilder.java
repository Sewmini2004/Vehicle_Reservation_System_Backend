package com.example.Vehicle_Reservation_System_Backend.utils;

import com.example.Vehicle_Reservation_System_Backend.dto.CustomerDTO;

public class EmailBuilder {
    private StringBuilder emailContent;

    public EmailBuilder() {
        emailContent = new StringBuilder();
    }

    public EmailBuilder addGreeting(String customerName) {
        emailContent.append("Dear ").append(customerName).append(",\n\n");
        return this;
    }

    public EmailBuilder addThankYouMessage() {
        emailContent.append("Thank you for registering with our service!\n\n");
        return this;
    }

    public EmailBuilder addCustomerDetails(CustomerDTO customerDTO) {
        emailContent.append("We are pleased to confirm your registration. Your details are as follows:\n")
                .append("Name: ").append(customerDTO.getName()).append("\n")
                .append("Email: ").append(customerDTO.getEmail()).append("\n")
                .append("Phone: ").append(customerDTO.getPhoneNumber()).append("\n")
                .append("Registration Date: ").append(customerDTO.getRegistrationDate()).append("\n\n");
        return this;
    }

    public EmailBuilder addClosing() {
        emailContent.append("Best Regards,\nYour Service Team");
        return this;
    }

    public String build() {
        return emailContent.toString();
    }
}
