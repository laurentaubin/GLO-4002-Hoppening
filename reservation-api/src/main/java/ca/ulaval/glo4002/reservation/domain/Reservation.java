package ca.ulaval.glo4002.reservation.domain;

public class Reservation {

  private final long id;

  public Reservation(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }
}
