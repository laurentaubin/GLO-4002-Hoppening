package ca.ulaval.glo4002.reservation.domain;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class ReservationTest {

  private static final long AN_ID = 123;
  private static final long ANOTHER_ID = 213241;

  @Test
  public void whenCreatingReservation_thenIdIsNotNull() {
    // when
    Reservation reservation = new Reservation(AN_ID);

    // then
    assertThat(reservation.getId()).isNotNull();
  }

  @Test
  public void whenCreatingTwoReservations_thenIdsAreNotEqual() {
    // when
    Reservation firstReservation = new Reservation(AN_ID);
    Reservation secondReservation = new Reservation(ANOTHER_ID);

    // then
    assertThat(firstReservation.getId()).isNotEqualTo(secondReservation.getId());
  }
}
