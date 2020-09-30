package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegetarian;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VegetarianStarter extends Recipe {
  private static final double PUMPKIN_QUANTITY = 5;
  private static final double CHOCOLATE_QUANTITY = 8;

  public VegetarianStarter() {
    ingredients.add(new Ingredient(IngredientName.PUMPKIN, PUMPKIN_QUANTITY));
    ingredients.add(new Ingredient(IngredientName.CHOCOLATE, CHOCOLATE_QUANTITY));
  }
}
