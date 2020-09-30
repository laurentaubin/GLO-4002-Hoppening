package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegan;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VeganMain extends Recipe {
  private static final double KIMCHI_QUANTITY = 10;

  public VeganMain() {
    ingredients.add(new Ingredient(IngredientName.KIMCHI, KIMCHI_QUANTITY));
  }
}
