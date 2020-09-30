package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegan;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VeganDessert extends Recipe {
  private static final double WORCESTERSHIRE_SAUCE_QUANTITY = 5;

  public VeganDessert() {
    ingredients.add(new Ingredient(IngredientName.WORCESTERSHIRE_SAUCE,
                                   WORCESTERSHIRE_SAUCE_QUANTITY));
  }
}
