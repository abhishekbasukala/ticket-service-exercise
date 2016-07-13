package com.walmart.store.recruiting.ticket.service.impl;

import com.walmart.store.recruiting.ticket.domain.ReserveSeat;
import com.walmart.store.recruiting.ticket.domain.SeatHold;
import com.walmart.store.recruiting.ticket.domain.Venue;
import com.walmart.store.recruiting.ticket.service.TicketService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A ticket service implementation.
 */
public class TicketServiceImpl implements TicketService {

	private int seatsAvailable;
	private int seatsReserved;
	private final int timeout = 10000;

	private int nextSeatsAvailable;
	// private Map<String, SeatHold> seatHoldMap = new HashMap<>();

	// modified Hashmap to ConcurrentHashMap
	private Map<String, SeatHold> seatHoldMap = new ConcurrentHashMap<>();
	private Map<String, ReserveSeat> reserveSeatMap = new ConcurrentHashMap<>();

	public TicketServiceImpl(Venue venue) {
		seatsAvailable = venue.getMaxSeats();
		nextSeatsAvailable = venue.getNextSeatsAvailable();
	}

	@Override
	public int numSeatsAvailable() {

		return seatsAvailable;
	}
	
	@Override
	public Map<String, ReserveSeat> getReservedSeats() {
		// TODO Auto-generated method stub
		return reserveSeatMap;
	}

	@Override
	public Map<String, SeatHold> getHoldedSeats() {
		// TODO Auto-generated method stub
		return seatHoldMap;
	}

	public int numSeatsReserved() {
		return this.seatsReserved;
	}

	public int getNextSeatsAvailable() {
		return this.nextSeatsAvailable;
	}

	@Override
	// one thread accessibility at a time using synchronized method
	public synchronized Optional<SeatHold> findAndHoldSeats(int numSeats) {

		Optional<SeatHold> optionalSeatHold = Optional.empty();

		if (seatsAvailable >= numSeats) {
			String holdId = generateId();
			SeatHold seatHold = new SeatHold(holdId, numSeats);
			optionalSeatHold = Optional.of(seatHold);
			seatHoldMap.put(holdId, seatHold);
			seatsAvailable -= numSeats;
			nextSeatsAvailable += numSeats;
			MyTimeHolder myTimeHolder = new MyTimeHolder(holdId);
			myTimeHolder.start();

		}

		return optionalSeatHold;

	}

	@Override
	public Optional<String> reserveSeats(String seatHoldId) {
		Optional<String> optionalReservation = Optional.empty();

		SeatHold seatHold = seatHoldMap.get(seatHoldId);
		if (seatHold != null) {
			seatsReserved += seatHold.getNumSeats();
			optionalReservation = Optional.of(seatHold.getId());
			// new object of ReserveSeat is created
			ReserveSeat reserveSeat = new ReserveSeat(seatHold);
			reserveSeatMap.put(seatHoldId, reserveSeat);
			seatHoldMap.remove(seatHoldId);

		} else {

			System.out.println("Your session has timed out or the hold id is missing");

		}

		return optionalReservation;

	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	// class to hold the time for 5 seconds
	class MyTimeHolder extends Thread {

		private String holdId;

		MyTimeHolder(String holdId) {
			this.holdId = holdId;
		}

		/**
		 * @param seats
		 *            to be deleted on hold if not reserved in 5 seconds
		 */
		public synchronized void deleteHoldSeats(int seats) {
			seatsAvailable += seats;
			nextSeatsAvailable -= seats;

		}

		public void run() {

			try {
				// waits for 5 seconds
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			// deletes the seats holded
			deleteHoldSeats(seatHoldMap.get(holdId).getNumSeats());
			seatHoldMap.remove(holdId);
		}

	}

}
