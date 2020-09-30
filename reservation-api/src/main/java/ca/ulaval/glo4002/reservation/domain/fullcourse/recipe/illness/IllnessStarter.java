package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.illness;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class IllnessStarter extends Recipe {
  private static final double SCALLOPS_QUANTITY = 2;
  private static final double BUTTERNUT_SQUASH_QUANTITY = 8;

  public IllnessStarter() {
    ingredients.add(new Ingredient(IngredientName.SCALLOPS, SCALLOPS_QUANTITY));
    ingredients.add(new Ingredient(IngredientName.BUTTERNUT_SQUASH, BUTTERNUT_SQUASH_QUANTITY));
  }
}
