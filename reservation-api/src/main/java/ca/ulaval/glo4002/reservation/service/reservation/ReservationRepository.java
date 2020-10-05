package ca.ulaval.glo4002.reservation.service.reservation;

import java.time.LocalDateTime;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;

public interface ReservationRepository {

  long createReservation(Reservation reservation);

  Reservation getReservationById(long reservationId);

  int getTotalNumberOfCustomersForADay(LocalDateTime date);
}
