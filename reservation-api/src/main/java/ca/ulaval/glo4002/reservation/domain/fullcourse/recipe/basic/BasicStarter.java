package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.basic;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class BasicStarter extends Recipe {
  private static final double PORK_LOIN_QUANTITY = 5;
  private static final double CARROTS_QUANTITY = 8;

  public BasicStarter() {
    ingredients.add(new Ingredient(IngredientName.PORK_LOIN, PORK_LOIN_QUANTITY));
    ingredients.add(new Ingredient(IngredientName.CARROTS, CARROTS_QUANTITY));
  }
}
