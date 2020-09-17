package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import org.junit.jupiter.api.function.Executable;


public class InMemoryReservationDaoTest {
  private static final long AN_ID = 123;
  private static final long ANOTHER_ID = 1234;

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

  @Test
  public void givenValidIdWithExistingReservations_whenGettingReservationFromId_thenReturnReservation() {
    // given
    Reservation expectedReservation = givenAReservation(AN_ID);
    Reservation secondReservation = givenAReservation(ANOTHER_ID);
    inMemoryReservationDao.createReservation(expectedReservation);
    inMemoryReservationDao.createReservation(secondReservation);

    // when
    Reservation actualReservation = inMemoryReservationDao.getReservationById(AN_ID);

    //then
    assertThat(actualReservation).isEqualTo(expectedReservation);
  }

  @Test
  public void givenInvalidIdWithExistingReservation_whenGettingReservationFromId_thenThrowNonExistingReservationException() {
    // given
    Reservation expectedReservation = givenAReservation(AN_ID);
    inMemoryReservationDao.createReservation(expectedReservation);

    // when
    Executable gettingReservation = () -> inMemoryReservationDao.getReservationById(ANOTHER_ID);

    // then
    assertThrows(NonExistingReservationException.class, gettingReservation);
  }

  private Reservation givenAReservation(long id) {
    return new ReservationBuilder().withId(id).withAnyTable().build();
  }
}
