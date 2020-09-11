package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;

public class InMemoryReservationDaoTest {

  private static final long AN_ID = 123;

  private InMemoryReservationDao inMemoryReservationDao;

  @BeforeEach
  public void setUp() {
    inMemoryReservationDao = new InMemoryReservationDao();
  }

  @Test
  public void whenCreatingNewReservationDao_thenPersistenceIsEmpty() {
    // when
    InMemoryReservationDao newlyCreatedReservationDao = new InMemoryReservationDao();

    // then
    assertThat(newlyCreatedReservationDao.getReservations()).isEmpty();
  }

  @Test
  public void givenEmptyPersistence_whenInsertingNewReservation_thenAppendThatElementToReservationList() {
    // given
    Reservation aReservation = givenAReservation(AN_ID);

    // when
    inMemoryReservationDao.createReservation(aReservation);

    // then
    assertThat(inMemoryReservationDao.getReservations()).hasSize(1);
    assertThat(inMemoryReservationDao.getReservations()).contains(aReservation);
  }

  @Test
  public void givenEmptyPersistence_whenInsertingNewReservation_thenReturnReservationId() {
    // given
    Reservation aReservation = givenAReservation(AN_ID);

    // when
    long expectedId = inMemoryReservationDao.createReservation(aReservation);

    // then
    assertThat(expectedId).isEqualTo(AN_ID);
  }

  private Reservation givenAReservation(long id) {
    return new ReservationBuilder().withId(id).withAnyTable().build();
  }
}
