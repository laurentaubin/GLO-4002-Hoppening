package ca.ulaval.glo4002.reservation.service;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.generator.id.IdGenerator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

  private static final long AN_ID = 4321;
  private static final long ANOTHER_ID = 8817;

  @Mock
  private IdGenerator idGenerator;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private CreateReservationRequestDto createReservationRequestDto;

  @Mock
  private ReservationAssembler reservationAssembler;

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(idGenerator,
                                                reservationRepository,
                                                reservationAssembler);
  }

  @Test
  public void givenReservationServiceInInitialState_whenCreatingReservation_thenReturnReservationId() {
    // given
    Reservation reservation = givenAReservation(AN_ID);
    setUpReservationServiceMocks(reservation, AN_ID);
    when(idGenerator.getLongUuid()).thenReturn(AN_ID);

    // when
    long reservationId = reservationService.createReservation(createReservationRequestDto);

    // then
    assertThat(reservationId).isEqualTo(AN_ID);
  }

  @Test
  public void givenReservationServiceInInitialState_whenCreatingTwoReservations_thenTheTwoReturnedIdsAreDifferent() {
    // given
    Reservation firstReservation = givenAReservation(AN_ID);
    Reservation secondReservation = givenAReservation(ANOTHER_ID);
    setUpReservationServiceMocks(firstReservation, AN_ID);
    setUpReservationServiceMocks(secondReservation, ANOTHER_ID);
    when(idGenerator.getLongUuid()).thenReturn(AN_ID).thenReturn(ANOTHER_ID);

    // when
    long firstId = reservationService.createReservation(createReservationRequestDto);
    long secondId = reservationService.createReservation(createReservationRequestDto);

    // then
    assertThat(firstId).isNotEqualTo(secondId);
  }

  private void setUpReservationServiceMocks(Reservation reservation, long id) {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       id)).willReturn(reservation);
    given(reservationRepository.createReservation(reservation)).willReturn(id);
  }

  private Reservation givenAReservation(long id) {
    return new Reservation(id);
  }
}
