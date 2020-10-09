package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;

public class ReservationRepository

{
  private final ReservationDao reservationDao;

  public ReservationRepository(ReservationDao reservationDao) {
    this.reservationDao = reservationDao;
  }

  public ReservationId createReservation(Reservation reservation) {
    return reservationDao.createReservation(reservation);
  }

  public Reservation getReservationById(ReservationId reservationId) {
    return reservationDao.getReservationById(reservationId);
  }

  public int getTotalNumberOfCustomersForADay(LocalDateTime date) {
    List<Reservation> reservations = reservationDao.getReservations();
    return getTotalNumberOfCustomersOfAllReservationsAtDinnerDate(date, reservations);
  }

  private boolean isTheSameDate(LocalDateTime date, LocalDateTime dinnerDate) {
    return LocalDate.from(date).isEqual(LocalDate.from(dinnerDate));
  }

  private int getTotalNumberOfCustomersOfAllReservationsAtDinnerDate(LocalDateTime date,
                                                                     List<Reservation> reservations)
  {
    int numberOfCustomers = 0;
    for (Reservation reservation : reservations) {
      if (isTheSameDate(date, reservation.getDinnerDate()))
        numberOfCustomers += reservation.getNumberOfCustomers();
    }
    return numberOfCustomers;
  }

  public List<Reservation> getReservationsByDate(LocalDateTime date) {
    List<Reservation> reservations = reservationDao.getReservations();
    List<Reservation> reservationsByDate = new ArrayList<>();

    for (Reservation reservation : reservations) {
      if (reservation.getDinnerDate().toLocalDate().isEqual(date.toLocalDate())) {
        reservationsByDate.add(reservation);
      }
    }

    return reservationsByDate;
  }
}
