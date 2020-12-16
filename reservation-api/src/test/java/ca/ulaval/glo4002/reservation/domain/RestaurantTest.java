package ca.ulaval.glo4002.reservation.domain;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.chef.ChefManager;
import ca.ulaval.glo4002.reservation.domain.date.Period;
import ca.ulaval.glo4002.reservation.domain.exception.ForbiddenReservationException;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningConfigurationRequest;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.material.Buffet;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.chef.NoChefsAvailableException;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

@ExtendWith(MockitoExtension.class)
public class RestaurantTest {
  private static final boolean CAUSE_ALLERGIC_CONFLICT = true;
  private static final boolean DOES_NOT_CAUSE_ALLERGIC_CONFLICT = false;
  private static final boolean ALL_INGREDIENTS_AVAILABLE = true;
  private static final boolean NOT_ALL_INGREDIENTS_AVAILABLE = false;
  private static final LocalDateTime A_DATE = LocalDateTime.of(2020, 7, 22, 23, 23);
  private static final int FORTY_ONE_CUSTOMERS = 41;
  private static final int TWO_CUSTOMERS = 2;
  private static final int ONE_CUSTOMER = 1;
  private static final LocalDate AN_OPENING_DATE = LocalDate.of(2020, 7, 20);
  private static final BigDecimal A_RESERVATION_FEE = BigDecimal.TEN;

  @Mock
  private ReservationFactory reservationFactory;

  @Mock
  private ReservationRequest reservationRequest;

  @Mock
  private ReservationBook reservationBook;

  @Mock
  private IngredientInventory ingredientInventory;

  @Mock
  private Reservation aReservation;

  @Mock
  private ReservationId expectedReservationId;

  @Mock
  private ReservationId aReservationId;

  @Mock
  private List<Reservation> reservations;

  @Mock
  private HoppeningEvent hoppeningEvent;

  @Mock
  private HoppeningConfigurationRequest hoppeningConfigurationRequest;

  @Mock
  private Buffet buffet;

  @Mock
  private ReportPeriod reportPeriod;

  @Mock
  private Period dinnerPeriod;

  @Mock
  private Map<LocalDate, DailyDishesQuantity> dailyDishesQuantity;

  @Mock
  private ChefManager chefManager;

  private Restaurant restaurant;

  @BeforeEach
  public void setUpRestaurant() {
    restaurant = new Restaurant(reservationFactory,
                                reservationBook,
                                ingredientInventory,
                                hoppeningEvent,
                                buffet,
                                chefManager);
  }

  @Test
  public void whenMakeReservation_thenReservationIsCreated() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(reservationFactory).create(reservationRequest, hoppeningEvent);
  }

  @Test
  public void whenMakeReservation_thenReservationIsRegisteredInReservationBook() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(reservationBook).register(aReservation);
  }

  @Test
  public void givenReservationCausingAnAllergicConflict_whenMakeReservation_thenReservationIsNotRegistered() {
    // given
    givenReservationRequestCausingAllergicConflict();

    // when
    try {
      restaurant.makeReservation(reservationRequest);
    } catch (Exception ignored) {
    }

    // then
    verify(reservationBook, times(0)).register(aReservation);
  }

  @Test
  public void givenReservationNotCausingAllergicConflict_whenMakeReservation_thenReservationIsRegistered() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(reservationBook).register(aReservation);
  }

  @Test
  public void whenMakeReservation_thenReservationIdOfTheCreatedReservationIsReturned() {
    // given
    givenValidReservationRequest();
    given(aReservation.getReservationId()).willReturn(expectedReservationId);

    // when
    ReservationId reservationId = restaurant.makeReservation(reservationRequest);

    // then
    assertThat(reservationId).isEqualTo(expectedReservationId);
  }

  @Test
  public void givenReservationCausingAllergicConflict_whenMakeReservation_thenThrowForbiddenReservationException() {
    // given
    givenReservationRequestCausingAllergicConflict();

    // when
    Executable makingReservation = () -> restaurant.makeReservation(reservationRequest);

    // then
    assertThrows(ForbiddenReservationException.class, makingReservation);
  }

  @Test
  public void givenNotAllIngredientsAvailableForReservation_whenMakeReservation_thenReservationIsRegistered() {
    // given
    givenNotAllIngredientsAreAvailable();

    // when
    Executable makingReservation = () -> restaurant.makeReservation(reservationRequest);

    // then
    assertThrows(ForbiddenReservationException.class, makingReservation);
  }

  @Test
  public void givenAnExistingReservation_whenGetReservationById_thenTheReservationIsRetrievedFromTheReservationBook() {
    // given
    given(reservationBook.getReservation(aReservationId)).willReturn(aReservation);

    // when
    Reservation reservation = restaurant.getReservation(aReservationId);

    // then
    assertThat(reservation).isEqualTo(aReservation);
  }

  @Test
  public void givenAReservationThatExceedMaximumCapacityOfCustomerForADay_whenMakeReservation_thenThrowTooManyPeopleException() {
    // given
    given(aReservation.getNumberOfCustomers()).willReturn(TWO_CUSTOMERS);
    given(aReservation.getDinnerDate()).willReturn(A_DATE);
    given(reservationBook.getNumberOfCustomersForADay(A_DATE)).willReturn(FORTY_ONE_CUSTOMERS);
    givenValidReservationRequest();

    // when
    Executable makingReservation = () -> restaurant.makeReservation(reservationRequest);

    // then
    assertThrows(TooManyPeopleException.class, makingReservation);
  }

  @Test
  public void givenAReservationThatExceedMaximumCapacityOfCustomerForADay_whenMakeReservation_thenReservationIsNotRegistered() {
    // given
    given(aReservation.getNumberOfCustomers()).willReturn(TWO_CUSTOMERS);
    given(aReservation.getDinnerDate()).willReturn(A_DATE);
    given(reservationBook.getNumberOfCustomersForADay(A_DATE)).willReturn(FORTY_ONE_CUSTOMERS);
    givenValidReservationRequest();

    // when
    try {
      restaurant.makeReservation(reservationRequest);
    } catch (Exception ignored) {
    }

    // then
    verify(reservationBook, times(0)).register(aReservation);
  }

  @Test
  public void givenAReservationThatDoesNotExceedMaxNumberOfCustomersForADay_whenMakeReservation_thenReservationIsRegistered() {
    // given
    given(aReservation.getNumberOfCustomers()).willReturn(ONE_CUSTOMER);
    given(aReservation.getDinnerDate()).willReturn(A_DATE);
    given(reservationBook.getNumberOfCustomersForADay(A_DATE)).willReturn(FORTY_ONE_CUSTOMERS);
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(reservationBook).register(aReservation);
  }

  @Test
  public void whenGetDailyDishesQuantity_thenGetDailyDishedQuantitiesFromBuffetIsCalled() {
    // given
    given(buffet.getDailyDishesQuantities(reportPeriod)).willReturn(dailyDishesQuantity);

    // when
    restaurant.getDailyDishesQuantity(reportPeriod);

    // then
    verify(buffet).getDailyDishesQuantities(reportPeriod);
  }

  @Test
  public void whenConfigureHoppeningEvent_thenHoppeningEventIsUpdated() {
    // when
    restaurant.configureHoppeningEvent(hoppeningConfigurationRequest);

    // then
    verify(hoppeningEvent).configureHoppening(hoppeningConfigurationRequest);
  }

  @Test
  public void givenAReservation_whenMakeReservation_thenNecessaryDishesAmountIsUpdated() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(buffet).updateDailyDishesQuantity(aReservation);
  }

  @Test
  public void whenMakeReservation_thenChefsAreHired() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(chefManager).hireChefsForReservations(reservations);
  }

  @Test
  public void givenTooPickyForChefs_whenMakeReservation_thenThrowForbiddenReservationException() {
    // given
    givenValidReservationRequest();
    doThrow(NoChefsAvailableException.class).when(chefManager)
                                            .hireChefsForReservations(reservations);

    // when
    Executable makingReservation = () -> restaurant.makeReservation(reservationRequest);

    // then
    assertThrows(ForbiddenReservationException.class, makingReservation);
  }

  @Test
  public void whenMakeReservation_thenVerifyIfThereIsAConflictCausedByAllergies() {
    // given
    givenValidReservationRequest();

    // when
    restaurant.makeReservation(reservationRequest);

    // then
    verify(ingredientInventory).doesReservationCauseAllergicConflict(aReservation, reservations);
  }

  @Test
  public void givenSomeReservations_whenCalculatingTotalIncome_thenReturnSumOfAllReservationIncomes() {
    // given
    given(reservationBook.getAllReservations()).willReturn(List.of(aReservation, aReservation));
    given(aReservation.getReservationFees()).willReturn(A_RESERVATION_FEE);

    // when
    BigDecimal totalFee = restaurant.calculateTotalReservationFee();

    // then
    assertThat(totalFee).isEqualTo(A_RESERVATION_FEE.add(A_RESERVATION_FEE));
  }

  private void givenValidReservationRequest() {
    given(dinnerPeriod.getStartDate()).willReturn(AN_OPENING_DATE);
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(reservationBook.getReservationsByDate(any())).willReturn(reservations);
    given(reservationFactory.create(reservationRequest, hoppeningEvent)).willReturn(aReservation);
    given(ingredientInventory.doesReservationCauseAllergicConflict(aReservation,
                                                                   reservations)).willReturn(DOES_NOT_CAUSE_ALLERGIC_CONFLICT);
    given(ingredientInventory.areAllNecessaryIngredientsAvailable(aReservation,
                                                                  AN_OPENING_DATE)).willReturn(ALL_INGREDIENTS_AVAILABLE);
  }

  private void givenReservationRequestCausingAllergicConflict() {
    given(reservationBook.getReservationsByDate(any())).willReturn(reservations);
    given(reservationFactory.create(reservationRequest, hoppeningEvent)).willReturn(aReservation);
    given(ingredientInventory.doesReservationCauseAllergicConflict(aReservation,
                                                                   reservations)).willReturn(CAUSE_ALLERGIC_CONFLICT);
  }

  private void givenNotAllIngredientsAreAvailable() {
    given(dinnerPeriod.getStartDate()).willReturn(AN_OPENING_DATE);
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(reservationBook.getReservationsByDate(any())).willReturn(reservations);
    given(reservationFactory.create(reservationRequest, hoppeningEvent)).willReturn(aReservation);
    given(ingredientInventory.doesReservationCauseAllergicConflict(aReservation,
                                                                   reservations)).willReturn(DOES_NOT_CAUSE_ALLERGIC_CONFLICT);
    given(ingredientInventory.areAllNecessaryIngredientsAvailable(aReservation,
                                                                  AN_OPENING_DATE)).willReturn(NOT_ALL_INGREDIENTS_AVAILABLE);
  }
}
