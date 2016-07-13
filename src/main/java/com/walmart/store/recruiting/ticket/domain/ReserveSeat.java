package com.walmart.store.recruiting.ticket.domain;

public class ReserveSeat {
	
	private String id;
    private String seatId;
    private int numSeats;

    
    public ReserveSeat(SeatHold seatHold){
    	this.id= seatHold.getId();
    	this.seatId = seatHold.getId();
    	this.numSeats = seatHold.getNumSeats();
    }


	public String getId() {
		return id;
	}


	public String getSeatId() {
		return seatId;
	}


	public int getNumSeats() {
		return numSeats;
	}

}
