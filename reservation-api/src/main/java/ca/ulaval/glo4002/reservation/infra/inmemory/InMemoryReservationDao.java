package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.Reservation;

public class InMemoryReservationDao {

  private final List<Reservation> reservations = new ArrayList<>();

  public List<Reservation> getReservations() {
    return reservations;
  }

  public long createReservation(Reservation reservation) {
    reservations.add(reservation);
    return reservation.getId();
  }
}
