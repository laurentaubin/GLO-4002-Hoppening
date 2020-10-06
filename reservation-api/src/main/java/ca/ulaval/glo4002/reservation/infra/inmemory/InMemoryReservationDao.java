package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;

public class InMemoryReservationDao {

  private final List<Reservation> reservations = new ArrayList<>();

  public List<Reservation> getReservations() {
    return reservations;
  }

  public ReservationId createReservation(Reservation reservation) {
    reservations.add(reservation);
    return reservation.getReservationId();
  }

  public Reservation getReservationById(ReservationId reservationId) {
    for (Reservation reservation : reservations) {
      if (reservationId.equals(reservation.getReservationId())) {
        return reservation;
      }
    }
    throw new NonExistingReservationException();
  }
}
