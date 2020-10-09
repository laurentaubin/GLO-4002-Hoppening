package ca.ulaval.glo4002.reservation.domain.fullcourse.stock;

import java.time.LocalDate;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;

public class TomatoStock implements Available {

  private final IngredientName ingredient;

  private final LocalDate tomatoAvailabilityDate;

  public TomatoStock(LocalDate openingDay,
                     IngredientName ingredient,
                     int ingredientPreparationDays)
  {
    this.ingredient = ingredient;
    this.tomatoAvailabilityDate = openingDay.plusDays(ingredientPreparationDays);
  }

  public boolean isAvailable(LocalDate dinnerDate) {
    return tomatoAvailabilityDate.isBefore(dinnerDate)
           || tomatoAvailabilityDate.isEqual(dinnerDate);
  }

  public IngredientName getIngredientName() {
    return ingredient;
  }
}
