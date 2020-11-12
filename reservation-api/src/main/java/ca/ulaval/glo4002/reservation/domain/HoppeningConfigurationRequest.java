package ca.ulaval.glo4002.reservation.domain;

public class HoppeningConfigurationRequest {
  private final Period dinnerPeriod;
  private final Period reservationPeriod;

  public HoppeningConfigurationRequest(Period dinnerEventPeriod, Period reservationEventPeriod) {
    this.dinnerPeriod = dinnerEventPeriod;
    this.reservationPeriod = reservationEventPeriod;
  }

  public Period getDinnerPeriod() {
    return this.dinnerPeriod;
  }

  public Period getReservationPeriod() {
    return this.reservationPeriod;
  }
}
