package com.api.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String firstName;
    String lastName;
    String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    @JsonIgnore
    private Booking booking;

    public Passenger() {
    }

    public Passenger(String firstName, String lastName, String gender, Booking bookingRecord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.booking = bookingRecord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Booking getBookingRecord() {
        return booking;
    }

    public void setBookingRecord(Booking booking) {
        this.booking = booking;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Passenger [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + "]";
    }
}
