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

import ca.ulaval.glo4002.reservation.api.reservation.builder.CreateReservationRequestDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.builder.ReservationDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.exception.ForbiddenReservationException;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationAuthorizer;
import ca.ulaval.glo4002.reservation.infra.ReservationRepository;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationService;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.exception.ReservationNotFoundException;
import ca.ulaval.glo4002.reservation.service.reservation.id.IdGenerator;
import ca.ulaval.glo4002.reservation.service.reservation.validator.ReservationValidator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  private static final long AN_ID = 4321;
  private static final String RESERVATION_NOT_FOUND_EXCEPTION = "Reservation with number 4321 not found";

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
  private IngredientQuantityRepository ingredientQuantityRepository;

  @Mock
  private ReservationAuthorizer reservationAuthorizer;

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(idGenerator,
                                                reservationRepository,
                                                ingredientQuantityRepository,
                                                reservationAssembler,
                                                reservationValidator,
                                                reservationAuthorizer);
  }

  @Test
  public void whenCreateReservation_thenReturnReservationId() {
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
    // given
    given(reservationAuthorizer.isReservationAllergicFriendly(any())).willReturn(true);

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
  public void givenReservationIsAllowed_whenCreateReservation_thenPersistenceIsUpdated() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    given(idGenerator.getLongUuid()).willReturn(AN_ID);
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       AN_ID)).willReturn(aReservation);
    given(reservationAuthorizer.isReservationAllergicFriendly(aReservation)).willReturn(true);

    // when
    reservationService.createReservation(createReservationRequestDto);

    // then
    verify(ingredientQuantityRepository).updateIngredientsQuantity(aReservation);
    verify(reservationRepository).createReservation(aReservation);
  }

  @Test
  public void givenReservationIsNotAllowed_whenCreateReservation_thenPersistenceIsNotUpdated() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    given(idGenerator.getLongUuid()).willReturn(AN_ID);
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       AN_ID)).willReturn(aReservation);
    given(reservationAuthorizer.isReservationAllergicFriendly(aReservation)).willReturn(false);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(ForbiddenReservationException.class, creatingReservation);
    verify(ingredientQuantityRepository, never()).updateIngredientsQuantity(aReservation);
    verify(reservationRepository, never()).createReservation(aReservation);
  }

  private void setUpReservationServiceMocksForIdTests(Reservation reservation, long id) {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto,
                                                                       id)).willReturn(reservation);
    given(reservationAuthorizer.isReservationAllergicFriendly(reservation)).willReturn(true);
    given(reservationRepository.createReservation(reservation)).willReturn(id);
  }
}
