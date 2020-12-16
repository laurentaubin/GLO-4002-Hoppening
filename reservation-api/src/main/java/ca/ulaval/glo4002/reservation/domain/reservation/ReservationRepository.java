package ca.ulaval.glo4002.reservation.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
  ReservationId saveReservation(Reservation reservation);

  Reservation getReservationById(ReservationId reservationId);

  List<Reservation> getReservationsByDate(LocalDateTime date);

  List<Reservation> getAllReservations();
}
