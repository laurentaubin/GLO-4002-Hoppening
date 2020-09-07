package ca.ulaval.glo4002.reservation.infra.inmemory;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.Reservation;

@ExtendWith(MockitoExtension.class)
public class InMemoryReservationRepositoryTest {

  private static final long AN_ID = 543;

  @Mock
  private InMemoryReservationDao reservationDao;

  @Mock
  private Reservation reservation;

  @Test
  public void whenCreatingReservation_thenReturnNewReservationId() {
    // given
    InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository(reservationDao);
    given(reservationDao.createReservation(reservation)).willReturn(AN_ID);

    // when
    long expectedReservationId = reservationRepository.createReservation(reservation);

    // then
    assertThat(expectedReservationId).isEqualTo(AN_ID);
  }
}
