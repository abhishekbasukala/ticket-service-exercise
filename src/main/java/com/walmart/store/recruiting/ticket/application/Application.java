package com.walmart.store.recruiting.ticket.application;

import java.util.Optional;

import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.impl.TicketServiceImpl;

public class Application {

	public static void main(String[] args) {
		TicketServiceImpl ticketServiceImp = new TicketServiceImpl(new Venue(100, 12, 20));
		System.out.println("Total number of next seat availabile: " + ticketServiceImp.getNextSeatsAvailable());
		System.out.println("Total number of seats available : " + ticketServiceImp.numSeatsAvailable());

		Optional<SeatHold> hold = ticketServiceImp.findAndHoldSeats(15);

		System.out.println("Total number of next seat availabile: " + ticketServiceImp.getNextSeatsAvailable());
		System.out.println("Total number of seats available : " + ticketServiceImp.numSeatsAvailable());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		System.out.println("Total number of next seat availabile:" + ticketServiceImp.getNextSeatsAvailable());
		System.out.println("Total number of seats available : " + ticketServiceImp.numSeatsAvailable());
		ticketServiceImp.reserveSeats(hold.get().getId());

		System.out.println("Total number of next seat availabile" + ticketServiceImp.getNextSeatsAvailable());
		System.out.println("Total number of seats available " + ticketServiceImp.numSeatsAvailable());

		Optional<SeatHold> holdtime = ticketServiceImp.findAndHoldSeats(10);
		ticketServiceImp.reserveSeats(holdtime.get().getId());

		System.out.println("Total number of next seat available" + ticketServiceImp.getNextSeatsAvailable());
		System.out.println("Total number of seats available" + ticketServiceImp.numSeatsAvailable());
		
	}
	
	

}
