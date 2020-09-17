package ca.ulaval.glo4002.reservation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.TableDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.exception.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.service.exception.InvalidReservationDateException;
import ca.ulaval.glo4002.reservation.service.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.validator.DinnerDateValidator;
import ca.ulaval.glo4002.reservation.service.validator.ReservationDateValidator;

@ExtendWith(MockitoExtension.class)
class ReservationValidatorTest {
  @Mock
  private DinnerDateValidator dinnerDateValidator;

  @Mock
  private ReservationDateValidator reservationDateValidator;

  public ReservationValidator reservationValidator;

  @BeforeEach
  public void setUp() {
    reservationValidator = new ReservationValidator(dinnerDateValidator, reservationDateValidator);
  }

  @Test
  public void givenValidRequest_whenValidate_thenDoNotThrow() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertDoesNotThrow(validatingReservation);
  }

  @Test
  public void givenEmptyTables_whenValidate_thenThrowInvalidReservationQuantityException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingReservation);
  }

  @Test
  public void givenTableWithEmptyCustomers_whenValidate_thenThrowInvalidReservationQuantityException() {
    // given
    TableDto tableDto = new TableDtoBuilder().build();
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withTable(tableDto)
                                                                                                      .build();

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationQuantityException.class, validatingReservation);
  }

  @Test
  public void givenOutOfBoundDinnerDate_whenValidate_thenThrowInvalidDinnerDateException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();
    doThrow(InvalidDinnerDateException.class).when(dinnerDateValidator).validate(any());

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidDinnerDateException.class, validatingReservation);
  }

  @Test
  public void givenAnyOutOfBoundReservationDate_whenValidate_thenThrowInvalidReservationDateException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withAnyTable()
                                                                                                      .build();
    doThrow(InvalidReservationDateException.class).when(reservationDateValidator).validate(any());

    // when
    Executable validatingReservation = () -> reservationValidator.validate(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationDateException.class, validatingReservation);
  }
}
