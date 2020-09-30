package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.illness;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class IllnessDessert extends Recipe {
  private static final double PEPPERONI_QUANTITY = 2;

  public IllnessDessert() {
    ingredients.add(new Ingredient(IngredientName.PEPPERONI, PEPPERONI_QUANTITY));
  }
}
