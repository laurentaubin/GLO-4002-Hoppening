package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationRepository;
import ca.ulaval.glo4002.reservation.domain.exception.ReservationNotFoundException;

public class InMemoryReservationRepository implements ReservationRepository {
  private final List<Reservation> reservations = new ArrayList<>();

  public ReservationId saveReservation(Reservation reservation) {
    reservations.add(reservation);
    return reservation.getReservationId();
  }

  public Reservation getReservationById(ReservationId reservationId) {
    for (Reservation reservation : reservations) {
      if (reservationId.equals(reservation.getReservationId())) {
        return reservation;
      }
    }
    throw new ReservationNotFoundException(reservationId);
  }

  public List<Reservation> getReservationsByDate(LocalDateTime date) {
    List<Reservation> reservationsByDate = new ArrayList<>();

    for (Reservation reservation : reservations) {
      if (reservation.getDinnerDate().toLocalDate().isEqual(date.toLocalDate())) {
        reservationsByDate.add(reservation);
      }
    }

    return reservationsByDate;
  }

  public List<Reservation> getAllReservations() {
    return reservations;
  }
}
