package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.illness;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class IllnessMain extends Recipe {
  private static final double KIWI_QUANTITY = 5;

  public IllnessMain() {
    ingredients.add(new Ingredient(IngredientName.KIWI, KIWI_QUANTITY));
  }
}
