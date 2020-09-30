package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.drink;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class Water extends Recipe {
  private static final double WATER_QUANTITY = 0.1;

  public Water() {
    ingredients.add(new Ingredient(IngredientName.WATER, WATER_QUANTITY));
  }
}