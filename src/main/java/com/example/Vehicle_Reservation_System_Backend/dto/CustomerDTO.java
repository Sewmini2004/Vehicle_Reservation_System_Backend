package com.example.Vehicle_Reservation_System_Backend.dto;

public class CustomerDTO {
    private int customerId;
    private int userId;
    private String name;
    private String address;
    private String nic;
    private String phoneNumber;
    private String registrationDate;
    private String email;

    private CustomerDTO(Builder builder) {
        this.customerId = builder.customerId;
        this.userId = builder.userId;
        this.name = builder.name;
        this.address = builder.address;
        this.nic = builder.nic;
        this.phoneNumber = builder.phoneNumber;
        this.registrationDate = builder.registrationDate;
        this.email = builder.email;
    }

    public static class Builder {
        private int customerId;
        private int userId;
        private String name;
        private String address;
        private String nic;
        private String phoneNumber;
        private String registrationDate;
        private String email;

        public Builder customerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder nic(String nic) {
            this.nic = nic;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder registrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(this);
        }
    }

    // Getters for all fields
    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNic() {
        return nic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getEmail() {
        return email;
    }
}
