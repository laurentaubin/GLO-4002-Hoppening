package ca.ulaval.glo4002.reservation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.exception.InvalidReservationQuantityException;

class ReservationValidatorTest {

  public ReservationValidator reservationValidator;

  @BeforeEach
  public void setUp() {
    reservationValidator = new ReservationValidator();
  }

  @Test
  public void givenValidRequest_whenValidating_thenDoNotThrow() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertDoesNotThrow(validatingReservation);
  }

  @Test
  public void givenEmptyTables_whenValidating_thenThrowInvalidReservationQuantityException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingReservation);
  }

  @Test
  public void givenTableWithEmptyCustomers_whenValidating_thenThrowInvalidReservationQuantityException() {
    // given
    TableDto tableDto = new TableDtoBuilder().build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingReservation);
  }
}
