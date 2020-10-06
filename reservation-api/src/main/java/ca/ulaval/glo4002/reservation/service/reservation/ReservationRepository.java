package ca.ulaval.glo4002.reservation.service.reservation;

import java.time.LocalDateTime;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;

public interface ReservationRepository {

  ReservationId createReservation(Reservation reservation);

  Reservation getReservationById(ReservationId reservationId);

  int getTotalNumberOfCustomersForADay(LocalDateTime date);
}
