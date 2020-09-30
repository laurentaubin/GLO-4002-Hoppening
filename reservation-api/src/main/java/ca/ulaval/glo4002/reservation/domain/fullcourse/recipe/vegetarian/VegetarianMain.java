package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegetarian;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VegetarianMain extends Recipe {
  private static final double TUNA_QUANTITY = 10;

  public VegetarianMain() {
    ingredients.add(new Ingredient(IngredientName.TUNA, TUNA_QUANTITY));
  }
}
