package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.basic;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class BasicMain extends Recipe {
  private static final double PEPPERONI_QUANTITY = 10;

  public BasicMain() {
    ingredients.add(new Ingredient(IngredientName.PEPPERONI, PEPPERONI_QUANTITY));
  }
}