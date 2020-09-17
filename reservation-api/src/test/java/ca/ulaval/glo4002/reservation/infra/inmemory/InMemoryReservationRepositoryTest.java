package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.Reservation;

@ExtendWith(MockitoExtension.class)
public class InMemoryReservationRepositoryTest {

  private static final long AN_ID = 543;
  private InMemoryReservationRepository reservationRepository;
  private Reservation reservation;

  @Mock
  private InMemoryReservationDao reservationDao;


  @BeforeEach
  public void setUp() {
    reservationRepository = new InMemoryReservationRepository(reservationDao);
    reservation = new ReservationBuilder().withId(AN_ID).withAnyTable().build();
  }

  @Test
  public void whenCreatingReservation_thenReturnNewReservationId() {
    // given
    given(reservationDao.createReservation(reservation)).willReturn(AN_ID);

    // when
    long expectedReservationId = reservationRepository.createReservation(reservation);

    // then
    assertThat(expectedReservationId).isEqualTo(AN_ID);
  }

  @Test
  public void whenGettingReservationById_thenReturnProperReservation() {
    // given
    given(reservationDao.getReservationById(AN_ID)).willReturn(reservation);

    // when
    Reservation actualReservation = reservationRepository.getReservationById(AN_ID);

    // then
    assertThat(actualReservation).isEqualTo(reservation);
  }
}
