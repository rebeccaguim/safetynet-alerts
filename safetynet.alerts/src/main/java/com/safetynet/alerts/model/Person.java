package com.safetynet.alerts.model;

/**
 * Represents a person in the system.
 */
public class Person {

    // First name of the person
    private String firstName;

    // Last name of the person
    private String lastName;

    // Address of the person
    private String address;

    // City where the person lives
    private String city;

    // Zip code of the address
    private String zip;

    // Phone number of the person
    private String phone;

    // Email address of the person
    private String email;

    // Default constructor
    public Person() {}

    /**
     * Gets the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the zip code
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip code
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}