package com.api.book.controller;

import com.api.book.entity.Booking;
import com.api.book.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class is controller
 */
@RestController
@CrossOrigin
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingComponent;

    /**
     * This method books the flight ticket and add to the databse
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    long book(@RequestBody Booking record) {
        System.out.println("Booking Request" + record);
        return bookingComponent.book(record);
    }

    /**
     * This method return the booking details
     * @param id
     * @return booking deatils
     */
    @RequestMapping("/get/{id}")
    Booking getBooking(@PathVariable long id) {
        return bookingComponent.getBooking(id);
    }
}
