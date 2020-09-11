package ca.ulaval.glo4002.reservation.infra.inmemory;

import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;

public class InMemoryReservationRepository implements ReservationRepository {
  private final InMemoryReservationDao inMemoryReservationDao;

  public InMemoryReservationRepository(InMemoryReservationDao inMemoryReservationDao) {
    this.inMemoryReservationDao = inMemoryReservationDao;
  }

  @Override
  public long createReservation(Reservation reservation) {
    return inMemoryReservationDao.createReservation(reservation);
  }
}
