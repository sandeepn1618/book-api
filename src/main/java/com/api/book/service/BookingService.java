package com.api.book.service;

import com.api.book.entity.Booking;
import com.api.book.entity.Inventory;
import com.api.book.entity.Passenger;
import com.api.book.repository.BookingRepository;
import com.api.book.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service
public class BookingService {
	private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
	//private static final String FareURL = "http://localhost:8082";

	@Autowired
	private  BookingRepository bookingRepository;
	@Autowired
	private InventoryRepository inventoryRepository;




	public long book(Booking record) {
		logger.info("calling fares to get fare");
	
		//check fare
		logger.info("calling inventory to get inventory");
		//check inventory
		Inventory inventory = inventoryRepository.findByFlightNumberAndFlightDate(record.getFlightNumber(),record.getFlightDate());
		if(!inventory.isAvailable(record.getPassengers().size())){
			throw new BookingException("No more seats avaialble");
		}
		logger.info("successfully checked inventory" + inventory);
		logger.info("calling inventory to update inventory");
		//update inventory
		inventory.setAvailable(inventory.getAvailable() - record.getPassengers().size());
		inventoryRepository.saveAndFlush(inventory);
		logger.info("sucessfully updated inventory");
		//save booking
		record.setStatus(BookingStatus.BOOKING_CONFIRMED); 
		Set<Passenger> passengers = record.getPassengers();
		passengers.forEach(passenger -> passenger.setBookingRecord(record));
		record.setBookingDate(new Date());
		long id=  bookingRepository.save(record).getId();
		logger.info("Successfully saved booking");
		//send a message to search to update inventory
		logger.info("sending a booking event");
		Map<String, Object> bookingDetails = new HashMap<String, Object>();
		bookingDetails.put("FLIGHT_NUMBER", record.getFlightNumber());
		bookingDetails.put("FLIGHT_DATE", record.getFlightDate());
		bookingDetails.put("NEW_INVENTORY", inventory.getBookableInventory());
		logger.info("booking event successfully delivered "+ bookingDetails);
		return id;
	} 

	public Booking getBooking(long id) {
		return bookingRepository.findById(new Long(id)).get();
	} 
	
	
	public void updateStatus(String status, long bookingId) {
		Booking record = bookingRepository.findById(new Long(bookingId)).get();
		record.setStatus(status);
	}
	

	
	private void checkFare(String requestedFare, String actualfare){
		logger.info("calling fares to get fare (reactively collected) "+ actualfare);
		if (!requestedFare.equals(actualfare))
			throw new BookingException("fare is tampered");
	}
}
