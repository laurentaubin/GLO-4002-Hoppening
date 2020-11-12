package ca.ulaval.glo4002.reservation.domain;

public class HoppeningEvent {
  private Period dinnerPeriod;
  private Period reservationPeriod;

  public HoppeningEvent(Period dinnerPeriod, Period reservationPeriod) {
    this.dinnerPeriod = dinnerPeriod;
    this.reservationPeriod = reservationPeriod;
  }

  public void configureHoppening(HoppeningConfigurationRequest hoppeningConfigurationRequest) {
    dinnerPeriod = hoppeningConfigurationRequest.getDinnerPeriod();
    reservationPeriod = hoppeningConfigurationRequest.getReservationPeriod();
  }

  public Period getDinnerPeriod() {
    return dinnerPeriod;
  }

  public Period getReservationPeriod() {
    return reservationPeriod;
  }
}
