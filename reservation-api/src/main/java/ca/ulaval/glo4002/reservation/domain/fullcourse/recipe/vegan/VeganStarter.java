package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegan;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VeganStarter extends Recipe {
  private static final double TOMATO_QUANTITY = 5;
  private static final double KIWI_QUANTITY = 8;

  public VeganStarter() {
    ingredients.add(new Ingredient(IngredientName.TOMATO, TOMATO_QUANTITY));
    ingredients.add(new Ingredient(IngredientName.KIWI, KIWI_QUANTITY));
  }
}
