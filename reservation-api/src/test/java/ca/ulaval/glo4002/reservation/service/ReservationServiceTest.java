package ca.ulaval.glo4002.reservation.service;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.AllergiesValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.validator.ReservationValidator;
import ca.ulaval.glo4002.reservation.infra.exception.NonExistingReservationException;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.inmemory.ReservationRepository;
import ca.ulaval.glo4002.reservation.service.reservation.ReservationService;
import ca.ulaval.glo4002.reservation.service.reservation.assembler.ReservationAssembler;
import ca.ulaval.glo4002.reservation.service.reservation.exception.ReservationNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
  private static final long AN_ID = 4321;
  private static final String RESERVATION_NOT_FOUND_EXCEPTION = "Reservation with number 4321 not found";

  @Mock
  private Reservation aReservation;

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
  private ReservationId reservationId;

  @Mock
  private IngredientAvailabilityValidator ingredientAvailabilityValidator;

  @Mock
  private AllergiesValidator allergiesValidator;

  private ReservationService reservationService;

  @BeforeEach
  public void setUp() {
    reservationService = new ReservationService(reservationRepository,
                                                ingredientQuantityRepository,
                                                reservationAssembler,
                                                reservationValidator,
                                                allergiesValidator,
                                                ingredientAvailabilityValidator);
  }

  @Test
  public void whenCreateReservation_thenReturnReservationId() {
    // given
    Reservation reservation = new ReservationBuilder().withId(reservationId).withAnyTable().build();
    setUpReservationServiceMocksForIdTests(reservation, reservationId);
    given(ingredientAvailabilityValidator.areIngredientsAvailableForReservation(any())).willReturn(true);

    // when
    ReservationId returnedReservationId = reservationService.createReservation(createReservationRequestDto);

    // then
    assertThat(returnedReservationId).isEqualTo(reservationId);
  }

  @Test
  public void whenCreateReservation_thenReservationValidatorIsCalled() {
    // given
    given(allergiesValidator.isReservationAllergicFriendly(any())).willReturn(true);
    given(ingredientAvailabilityValidator.areIngredientsAvailableForReservation(any())).willReturn(true);
    // when
    reservationService.createReservation(createReservationRequestDto);

    // then
    verify(reservationValidator).validate(createReservationRequestDto);
  }

  @Test
  public void whenGettingReservationDtoById_thenReservationDtoIsReturned() {
    // given
    Reservation expectedReservation = new ReservationBuilder().withId(reservationId)
                                                              .withAnyTable()
                                                              .build();
    ReservationDto expectedReservationDto = new ReservationDtoBuilder().withAnyCustomers().build();
    given(reservationRepository.getReservationById(reservationId)).willReturn(expectedReservation);
    given(reservationAssembler.assembleDtoFromReservation(expectedReservation)).willReturn(expectedReservationDto);

    // when
    ReservationDto actualReservationDto = reservationService.getReservationDtoById(reservationId);

    // then
    assertThat(actualReservationDto).isEqualTo(expectedReservationDto);
  }

  @Test
  public void givenMissingReservation_whenGettingReservationById_thenThrowReservationNotFoundException() {
    // given
    given(reservationId.getLongId()).willReturn(AN_ID);
    doThrow(NonExistingReservationException.class).when(reservationRepository)
                                                  .getReservationById(reservationId);

    // when
    Executable gettingReservation = () -> reservationService.getReservationDtoById(reservationId);

    // then
    ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class,
                                                          gettingReservation);
    assertThat(exception.getDescription()).isEqualTo(RESERVATION_NOT_FOUND_EXCEPTION);
  }

  @Test
  public void givenReservationIsAllowed_whenCreateReservation_thenPersistenceIsUpdated() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto)).willReturn(aReservation);
    given(allergiesValidator.isReservationAllergicFriendly(aReservation)).willReturn(true);
    given(ingredientAvailabilityValidator.areIngredientsAvailableForReservation(aReservation)).willReturn(true);

    // when
    reservationService.createReservation(createReservationRequestDto);

    // then
    verify(ingredientQuantityRepository).updateIngredientsQuantity(aReservation);
    verify(reservationRepository).createReservation(aReservation);
  }

  @Test
  public void givenReservationIsNotAllergicFriendly_whenCreateReservation_thenPersistenceIsNotUpdated() {
    // given
    CreateReservationRequestDto createReservationRequestDto = new CreateReservationRequestDtoBuilder().build();
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto)).willReturn(aReservation);
    given(allergiesValidator.isReservationAllergicFriendly(aReservation)).willReturn(false);

    // when
    Executable creatingReservation = () -> reservationService.createReservation(createReservationRequestDto);

    // then
    assertThrows(ForbiddenReservationException.class, creatingReservation);
    verify(ingredientQuantityRepository, never()).updateIngredientsQuantity(aReservation);
    verify(reservationRepository, never()).createReservation(aReservation);
  }

  private void setUpReservationServiceMocksForIdTests(Reservation reservation,
                                                      ReservationId reservationId)
  {
    given(reservationAssembler.assembleFromCreateReservationRequestDto(createReservationRequestDto)).willReturn(reservation);
    given(allergiesValidator.isReservationAllergicFriendly(reservation)).willReturn(true);

    given(reservationRepository.createReservation(reservation)).willReturn(reservationId);
  }
}
