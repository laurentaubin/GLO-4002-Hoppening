package ca.ulaval.glo4002.reservation.domain.fullcourse.stock;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationIngredientCalculator;

public class IngredientAvailabilityValidator {

  private final ReservationIngredientCalculator reservationIngredientCalculator;

  private final Set<Available> ingredientsStock;

  public IngredientAvailabilityValidator(ReservationIngredientCalculator reservationIngredientCalculator,
                                         Set<Available> ingredientsStock)
  {
    this.reservationIngredientCalculator = reservationIngredientCalculator;
    this.ingredientsStock = ingredientsStock;
  }

  public boolean areIngredientsAvailableForReservation(Reservation reservation) {
    for (Available ingredientStock : ingredientsStock) {
      if (doesReservationContainIngredient(reservation, ingredientStock.getIngredientName())
          && !(ingredientStock.isAvailable(reservation.getDinnerDate().toLocalDate())))
      {
        return false;
      }
    }
    return true;
  }

  private boolean doesReservationContainIngredient(Reservation reservation,
                                                   IngredientName ingredientName)
  {
    Map<IngredientName, BigDecimal> ingredientQuantity = reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);
    return ingredientQuantity.containsKey(ingredientName);
  }
}
