package ca.ulaval.glo4002.reservation.domain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.AllergiesDetector;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;

@ExtendWith(MockitoExtension.class)
public class IngredientInventoryTest {
  private static final LocalDateTime A_DINNER_DATE = LocalDateTime.of(2150, 7, 20, 3, 4);
  @Mock
  private IngredientQuantityRepository ingredientQuantityRepository;

  @Mock
  private IngredientAvailabilityValidator ingredientAvailabilityValidator;

  @Mock
  private AllergiesDetector allergiesDetector;

  @Mock
  private Reservation reservation;

  @Mock
  private List<Reservation> existingReservations;

  @Mock
  private Map<IngredientName, BigDecimal> dailyIngredients;

  private IngredientInventory ingredientInventory;

  @BeforeEach
  public void setUpIngredientInventory() {
    ingredientInventory = new IngredientInventory(ingredientQuantityRepository,
                                                  ingredientAvailabilityValidator,
                                                  allergiesDetector);
  }

  @Test
  public void whenAreAllIngredientsAvailable_thenIngredientsAvailabilityIsVerified() {
    // when
    ingredientInventory.areAllNecessaryIngredientsAvailable(reservation);

    // then
    verify(ingredientAvailabilityValidator).areIngredientsAvailableForReservation(reservation);
  }

  @Test
  public void whenDoesReservationCauseAllergicConflict_thenDailyIngredientsAreFetched() {
    // given
    given(reservation.getDinnerDate()).willReturn(A_DINNER_DATE);

    // when
    ingredientInventory.doesReservationCauseAllergicConflict(reservation, existingReservations);

    // then
    verify(ingredientQuantityRepository).getIngredientsQuantity(A_DINNER_DATE.toLocalDate());
  }

  @Test
  public void whenDoesReservationCauseAllergicConflict_thenVerifyIfReservationIsAllergicFriendly() {
    // given
    given(reservation.getDinnerDate()).willReturn(A_DINNER_DATE);
    given(ingredientQuantityRepository.getIngredientsQuantity(A_DINNER_DATE.toLocalDate())).willReturn(dailyIngredients);

    // when
    ingredientInventory.doesReservationCauseAllergicConflict(reservation, existingReservations);

    // then
    verify(allergiesDetector).isReservationAllergicFriendly(reservation,
                                                            existingReservations,
                                                            dailyIngredients);
  }

  @Test
  public void whenGetIngredientsAtDate_thenIngredientsAtDateAreFetched() {
    // when
    ingredientInventory.getIngredientsAtDate(A_DINNER_DATE.toLocalDate());

    // then
    verify(ingredientQuantityRepository).getIngredientsQuantity(A_DINNER_DATE.toLocalDate());
  }

  @Test
  public void whenUpdateIngredientInventory_thenIngredientQuantityRepositoryIsUpdated() {
    // when
    ingredientInventory.updateIngredientInventory(reservation);

    // then
    verify(ingredientQuantityRepository).updateIngredientsQuantity(reservation);
  }
}
