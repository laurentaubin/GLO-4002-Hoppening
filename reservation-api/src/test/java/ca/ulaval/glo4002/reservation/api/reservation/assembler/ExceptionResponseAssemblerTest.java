package ca.ulaval.glo4002.reservation.api.reservation.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.ExceptionResponse;
import ca.ulaval.glo4002.reservation.exception.ReservationException;
import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidDinnerDateException;

public class ExceptionResponseAssemblerTest {

  private ExceptionResponseAssembler assembler;

  @BeforeEach
  public void setup() {
    assembler = new ExceptionResponseAssembler();
  }

  @Test
  public void whenAssemblerExceptionResponse_thenReturnExceptionResponseWithCorrespondingErrorAndDescription() {
    // given
    ReservationException reservationException = new InvalidDinnerDateException();

    // when
    ExceptionResponse exceptionResponse = assembler.assembleExceptionResponseFromException(reservationException);

    // then
    assertThat(exceptionResponse.getError()).isEqualTo(reservationException.getError());
    assertThat(exceptionResponse.getDescription()).isEqualTo(reservationException.getDescription());
  }
}
