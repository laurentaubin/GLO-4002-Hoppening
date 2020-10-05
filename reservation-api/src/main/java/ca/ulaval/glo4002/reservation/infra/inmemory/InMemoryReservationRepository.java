package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationRepository;

public class InMemoryReservationRepository implements ReservationRepository {
  private final InMemoryReservationDao inMemoryReservationDao;

  public InMemoryReservationRepository(InMemoryReservationDao inMemoryReservationDao) {
    this.inMemoryReservationDao = inMemoryReservationDao;
  }

  public long createReservation(Reservation reservation) {
    return inMemoryReservationDao.createReservation(reservation);
  }

  public Reservation getReservationById(long reservationId) {
    return inMemoryReservationDao.getReservationById(reservationId);
  }

  public int getTotalNumberOfCustomersForADay(LocalDateTime date) {
    List<Reservation> reservations = inMemoryReservationDao.getReservations();
    return getTotalNumberOfCustomers(date, reservations);
  }

  private boolean isTheSameDate(LocalDateTime date, LocalDateTime dinnerDate) {
    return LocalDate.from(date).isEqual(LocalDate.from(dinnerDate));
  }

  private int getTotalNumberOfCustomers(LocalDateTime date, List<Reservation> reservations) {
    int numberOfCustomers = 0;
    for (Reservation reservation : reservations) {
      if (isTheSameDate(date, reservation.getDinnerDate()))
        for (Table table : reservation.getTables()) {
          numberOfCustomers += table.getCustomers().size();
        }
    }
    return numberOfCustomers;
  }
}
