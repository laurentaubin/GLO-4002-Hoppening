package ca.ulaval.glo4002.reservation.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;
import ca.ulaval.glo4002.reservation.domain.reservation.AllergiesDetector;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;

public class IngredientInventory {
  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientAvailabilityValidator ingredientAvailabilityValidator;
  private final AllergiesDetector allergiesDetector;

  public IngredientInventory(IngredientQuantityRepository ingredientQuantityRepository,
                             IngredientAvailabilityValidator ingredientAvailabilityValidator,
                             AllergiesDetector allergiesDetector)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientAvailabilityValidator = ingredientAvailabilityValidator;
    this.allergiesDetector = allergiesDetector;
  }

  public void updateIngredientInventory(Reservation reservation) {
    ingredientQuantityRepository.updateIngredientsQuantity(reservation);
  }

  public boolean doesReservationCauseAllergicConflict(Reservation reservation,
                                                      List<Reservation> existingReservations)
  {
    Map<IngredientName, BigDecimal> dailyIngredients = getIngredientsAtDate(reservation.getDinnerDate()
                                                                                       .toLocalDate());
    return !allergiesDetector.isReservationAllergicFriendly(reservation,
                                                            existingReservations,
                                                            dailyIngredients);
  }

  public boolean areAllNecessaryIngredientsAvailable(Reservation reservation,
                                                     LocalDate restaurantOpeningDate)
  {
    return ingredientAvailabilityValidator.areIngredientsAvailableForReservation(reservation,
                                                                                 restaurantOpeningDate);
  }

  public Map<IngredientName, BigDecimal> getIngredientsAtDate(LocalDate date) {
    return ingredientQuantityRepository.getIngredientsQuantity(date);
  }
}
