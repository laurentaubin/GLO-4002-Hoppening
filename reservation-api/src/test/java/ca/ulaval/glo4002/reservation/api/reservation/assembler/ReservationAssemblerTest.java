package ca.ulaval.glo4002.reservation.api.reservation.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;

class ReservationAssemblerTest {
  private static final long AN_ID = 123;

  private ReservationAssembler reservationAssembler;

  @BeforeEach
  public void setUp() {
    reservationAssembler = new ReservationAssembler();
  }

  @Test
  public void givenEmptyCreateReservationRequestDto_whenAssembleFromCreateReservationRequestDto_thenReturnValidReservation() {
    // given
    CreateReservationRequestDto createReservationRequestDto = givenACreateReservationRequestDto();

    // when
    Reservation expectedReservation = reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                                                   AN_ID);

    // then
    assertThat(expectedReservation.getId()).isEqualTo(AN_ID);
  }

  private CreateReservationRequestDto givenACreateReservationRequestDto() {
    return new CreateReservationRequestDto();
  }
}
