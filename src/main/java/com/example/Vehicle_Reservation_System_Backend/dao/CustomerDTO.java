package com.example.Vehicle_Reservation_System_Backend.dao;

public class CustomerDTO {
    private int customerId;
    private int userId;
    private String name;
    private String address;
    private String nic;
    private String phoneNumber;
    private String registrationDate;
    private String email;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerDTO() {

    }

    public CustomerDTO(int customerId, int userId, String name, String address, String nic, String phoneNumber, String registrationDate, String email) {
        this.customerId = customerId;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.nic = nic;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.email = email;
    }
}
