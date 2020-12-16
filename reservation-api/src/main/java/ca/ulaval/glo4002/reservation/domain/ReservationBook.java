package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationRepository;

public class ReservationBook {
  private final ReservationRepository reservationRepository;

  public ReservationBook(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public void register(Reservation reservation) {
    reservationRepository.saveReservation(reservation);
  }

  public Reservation getReservation(ReservationId reservationId) {
    return reservationRepository.getReservationById(reservationId);
  }

  public List<Reservation> getReservationsByDate(LocalDateTime date) {
    return reservationRepository.getReservationsByDate(date);
  }

  public int getNumberOfCustomersForADay(LocalDateTime dinnerDate) {
    int numberOfCustomers = 0;
    List<Reservation> reservations = reservationRepository.getReservationsByDate(dinnerDate);
    for (Reservation reservation : reservations) {
      numberOfCustomers = numberOfCustomers + reservation.getNumberOfCustomers();
    }
    return numberOfCustomers;
  }

  public List<Reservation> getAllReservations() {
    return reservationRepository.getAllReservations();
  }
}
