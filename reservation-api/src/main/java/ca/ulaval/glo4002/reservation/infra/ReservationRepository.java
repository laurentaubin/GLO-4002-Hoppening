package ca.ulaval.glo4002.reservation.infra;

import ca.ulaval.glo4002.reservation.domain.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository {

  long createReservation(Reservation reservation);

  Reservation getReservationById(long reservationId);

  int getTotalNumberOfCustomersForADay(LocalDateTime date);
}
