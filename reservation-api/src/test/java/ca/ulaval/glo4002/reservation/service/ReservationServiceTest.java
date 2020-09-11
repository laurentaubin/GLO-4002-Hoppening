package ca.ulaval.glo4002.reservation.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationErrorResponseDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.exception.InvalidReservationQuantityException;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

  private static final long AN_ID = 4321;

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

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(idGenerator,
                                                reservationRepository,
                                                reservationAssembler,
                                                reservationValidator);
  }

  @Test
  public void givenReservationServiceInInitialState_whenCreatingReservation_thenReturnReservationId() {
    // given
    Reservation reservation = new ReservationBuilder().withId(AN_ID).withAnyTable().build();
    setUpReservationServiceMocks(reservation, AN_ID);
    when(idGenerator.getLongUuid()).thenReturn(AN_ID);

    // when
    long reservationId = reservationService.createReservation(createReservationRequestDto);

    // then
    assertThat(reservationId).isEqualTo(AN_ID);
  }

  @Test
  public void givenEmptyTables_whenCreatingReservation_thenThrowWebApplicationExceptionWithCode400AndRightMessage() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    doThrow(new InvalidReservationQuantityException()).when(reservationValidator)
                                                      .validate(createReservationRequestDto);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    WebApplicationException actualThrow = assertThrows(WebApplicationException.class,
                                                       creatingReservation);
    CreateReservationErrorResponseDto createReservationErrorResponseDto = (CreateReservationErrorResponseDto) actualThrow.getResponse()
                                                                                                                         .getEntity();
    assertThat(createReservationErrorResponseDto.getDescription()).isEqualTo(ReservationErrorCode.INVALID_RESERVATION_QUANTITY.getMessage());
    assertThat(createReservationErrorResponseDto.getError()).isEqualTo(ReservationErrorCode.INVALID_RESERVATION_QUANTITY.toString());
    assertThat(actualThrow.getResponse()
                          .getStatus()).isEqualTo(ReservationErrorCode.INVALID_RESERVATION_QUANTITY.getCode());
  }

  private void setUpReservationServiceMocks(Reservation reservation, long id) {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       id)).willReturn(reservation);
    given(reservationRepository.createReservation(reservation)).willReturn(id);
  }
}
