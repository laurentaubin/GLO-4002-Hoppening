package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;

@ExtendWith(MockitoExtension.class)
public class InMemoryReservationDaoTest {
  private static final long RESERVATION_ID = 4321;

  @Mock
  private ReservationId aReservationId;

  @Mock
  private ReservationId anotherReservationId;

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
    Reservation aReservation = givenAReservation(aReservationId);

    // when
    inMemoryReservationDao.createReservation(aReservation);

    // then
    assertThat(inMemoryReservationDao.getReservations()).hasSize(1);
    assertThat(inMemoryReservationDao.getReservations()).contains(aReservation);
  }

  @Test
  public void givenEmptyPersistence_whenInsertingNewReservation_thenReturnReservationId() {
    // given
    Reservation aReservation = givenAReservation(aReservationId);

    // when
    ReservationId expectedId = inMemoryReservationDao.createReservation(aReservation);

    // then
    assertThat(expectedId).isEqualTo(aReservationId);
  }

  @Test
  public void givenValidIdWithExistingReservations_whenGettingReservationFromId_thenReturnReservation() {
    // given
    ReservationId reservationId = new ReservationId(RESERVATION_ID);
    ReservationId queryReservationId = new ReservationId(RESERVATION_ID);
    Reservation expectedReservation = givenAReservation(reservationId);
    inMemoryReservationDao.createReservation(expectedReservation);

    // when
    Reservation actualReservation = inMemoryReservationDao.getReservationById(queryReservationId);

    // then
    assertThat(actualReservation).isEqualTo(expectedReservation);
  }

  @Test
  public void givenInvalidIdWithExistingReservation_whenGettingReservationFromId_thenThrowNonExistingReservationException() {
    // given
    Reservation expectedReservation = givenAReservation(aReservationId);
    inMemoryReservationDao.createReservation(expectedReservation);

    // when
    Executable gettingReservation = () -> inMemoryReservationDao.getReservationById(anotherReservationId);

    // then
    assertThrows(NonExistingReservationException.class, gettingReservation);
  }

  private Reservation givenAReservation(ReservationId id) {
    return new ReservationBuilder().withId(id).withAnyTable().build();
  }
}
