package com.backend.vo;

public class Bundle {
    private String[] ticket;
    private String[] insurance;
    private String[] meal;
    private String[] hotel;
    private String[] seat;
    private String[] baggage;

    public Bundle() {
        this.ticket = new String[0];
        this.insurance = new String[0];
        this.meal = new String[0];
        this.hotel = new String[0];
        this.seat = new String[0];
        this.baggage = new String[0];
    }

    public String[] getTicket() {
        return ticket;
    }

    public void setTicket(String[] ticket) {
        this.ticket = ticket;
    }

    public String[] getInsurance() {
        return insurance;
    }

    public void setInsurance(String[] insurance) {
        this.insurance = insurance;
    }

    public String[] getMeal() {
        return meal;
    }

    public void setMeal(String[] meal) {
        this.meal = meal;
    }

    public String[] getHotel() {
        return hotel;
    }

    public void setHotel(String[] hotel) {
        this.hotel = hotel;
    }

    public String[] getSeat() {
        return seat;
    }

    public void setSeat(String[] seat) {
        this.seat = seat;
    }

    public String[] getBaggage() {
        return baggage;
    }

    public void setBaggage(String[] baggage) {
        this.baggage = baggage;
    }


}
