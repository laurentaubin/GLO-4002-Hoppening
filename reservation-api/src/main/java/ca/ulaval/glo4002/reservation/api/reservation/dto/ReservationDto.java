package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

public class ReservationDto {
    private String dinnerDate;
    private float reservationPrice;
    private List<CustomerDto> customers;

    public String getDinnerDate() {
        return dinnerDate;
    }

    public void setDinnerDate(String dinnerDate) {
        this.dinnerDate = dinnerDate;
    }

    public float getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(float reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public List<CustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }
}
