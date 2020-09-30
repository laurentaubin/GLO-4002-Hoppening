package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.allergy;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class AllergyStarter extends Recipe {
  private static final double MARMALADE_QUANTITY = 5;
  private static final double PLANTAIN_QUANTITY = 8;

  public AllergyStarter() {
    ingredients.add(new Ingredient(IngredientName.MARMALADE, MARMALADE_QUANTITY));
    ingredients.add(new Ingredient(IngredientName.PLANTAIN, PLANTAIN_QUANTITY));
  }
}
