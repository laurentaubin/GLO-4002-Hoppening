package ca.ulaval.glo4002.reservation.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.builder.ReservationDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.ReportRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.exception.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.validator.ReservationValidator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  private static final long AN_ID = 4321;
  private static final String RESERVATION_NOT_FOUND_EXCEPTION = "Reservation with number 4321 not found";
  private static final String A_DATE = "2150-11-16T23:59:59.999Z";

  @Mock
  private Reservation aReservation;

  @Mock
  private IdGenerator idGenerator;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private CreateReservationRequestDto createReservationRequestDto;

  @Mock
  private ReservationAssembler reservationAssembler;

  @Mock
  private ReservationValidator reservationValidator;

  @Mock
  private ReportRepository reportRepository;

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(idGenerator,
                                                reservationRepository,
                                                reportRepository,
                                                reservationAssembler,
                                                reservationValidator);
  }

  @Test
  public void givenReservationServiceInInitialState_whenCreateReservation_thenReturnReservationId() {
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
  public void whenCreateReservation_thenReservationValidatorIsCalled() {
    // when
    reservationService.createReservation(createReservationRequestDto);

    // then
    verify(reservationValidator).validate(createReservationRequestDto);
  }

  @Test
  public void whenGettingReservationDtoById_thenReservationDtoIsReturned() {
    // given
    Reservation expectedReservation = new ReservationBuilder().withId(AN_ID).withAnyTable().build();
    ReservationDto expectedReservationDto = new ReservationDtoBuilder().withAnyCustomers().build();
    given(reservationRepository.getReservationById(AN_ID)).willReturn(expectedReservation);
    given(reservationAssembler.assembleDtoFromReservation(expectedReservation)).willReturn(expectedReservationDto);

    // when
    ReservationDto actualReservationDto = reservationService.getReservationDtoById(AN_ID);

    // then
    assertThat(actualReservationDto).isEqualTo(expectedReservationDto);
  }

  @Test
  public void givenMissingReservation_whenGettingReservationById_thenThrowReservationNotFoundException() {
    // given
    doThrow(NonExistingReservationException.class).when(reservationRepository)
                                                  .getReservationById(AN_ID);

    // when
    Executable gettingReservation = () -> reservationService.getReservationDtoById(AN_ID);

    // then
    ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class,
                                                          gettingReservation);
    assertThat(exception.getDescription()).isEqualTo(RESERVATION_NOT_FOUND_EXCEPTION);
  }

  @Test
  public void whenCreateReservation_thenReportRepositoryIsCalled() {

    // given
    given(idGenerator.getLongUuid()).willReturn(AN_ID);
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       AN_ID)).willReturn(aReservation);

    // when
    reservationService.createReservation(createReservationRequestDto);

    // then
    verify(reportRepository).updateIngredientsQuantity(aReservation);
  }

  private void setUpReservationServiceMocksForIdTests(Reservation reservation, long id) {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       id)).willReturn(reservation);
    given(reservationRepository.createReservation(reservation)).willReturn(id);
  }
}
