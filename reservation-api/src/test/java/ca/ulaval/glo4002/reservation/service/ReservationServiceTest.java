package ca.ulaval.glo4002.reservation.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.exception.InvalidDinnerDateException;
import ca.ulaval.glo4002.reservation.service.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.validator.DinnerDateValidator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  private static final long AN_ID = 4321;
  private static final String INVALID_FORMAT_DINNER_DATE = "21-05-30";
  private static final String OUT_OF_BOUND_DINNER_DATE = "2020-07-30T23:59:59.999Z";

  @Mock
  private IdGenerator idGenerator;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private CreateReservationRequestDto createReservationRequestDto;

  @Mock
  private ReservationAssembler reservationAssembler;

  @Mock
  private DinnerDateValidator dinnerDateValidator;

  @Mock
  private ReservationValidator reservationValidator;

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(idGenerator,
                                                reservationRepository,
                                                reservationAssembler,
                                                dinnerDateValidator,
                                                reservationValidator);
  }

  @Test
  public void givenReservationServiceInInitialState_whenCreatingReservation_thenReturnReservationId() {
    // given
    Reservation reservation = new ReservationBuilder().withId(AN_ID).withAnyTable().build();
    setUpReservationServiceMocksForIdTests(reservation, AN_ID);
    when(idGenerator.getLongUuid()).thenReturn(AN_ID);

    // when
    long reservationId = reservationService.createReservation(createReservationRequestDto);

    // then
    assertThat(reservationId).isEqualTo(AN_ID);
  }

  @Test
  public void givenEmptyTables_whenCreatingReservation_thenThrowInvalidReservationQuantityException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    doThrow(new InvalidReservationQuantityException()).when(reservationValidator)
                                                      .validate(createReservationRequestDto);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidReservationQuantityException.class, creatingReservation);
  }

  @Test
  public void givenARequestWithoutDinnerDate_whenCreatingReservation_thenThrowInvalidFormatException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(null)
                                                                                                      .build();
    doThrow(InvalidFormatException.class).when(dinnerDateValidator).validateDate(any());

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidFormatException.class, creatingReservation);
  }

  @Test
  public void givenARequestWithInvalidFormatDinnerDate_whenCreatingReservation_thenThrowInvalidFormatException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(INVALID_FORMAT_DINNER_DATE)
                                                                                                      .build();
    doThrow(InvalidFormatException.class).when(dinnerDateValidator)
                                         .validateDate(INVALID_FORMAT_DINNER_DATE);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidFormatException.class, creatingReservation);
  }

  @Test
  public void givenARequestWithAnOutOfBoundDinnerDate_whenCreatingReservation_thenThrowInvalidDinnerDateException() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().withDinnerDate(OUT_OF_BOUND_DINNER_DATE)
                                                                                                      .build();
    doThrow(InvalidDinnerDateException.class).when(dinnerDateValidator)
                                             .validateDate(OUT_OF_BOUND_DINNER_DATE);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(InvalidDinnerDateException.class, creatingReservation);
  }

  private void setUpReservationServiceMocksForIdTests(Reservation reservation, long id) {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       id)).willReturn(reservation);
    given(reservationRepository.createReservation(reservation)).willReturn(id);
  }
}
