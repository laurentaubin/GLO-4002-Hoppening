package ca.ulaval.glo4002.reservation.domain.fullcourse.recipe.vegetarian;

import ca.ulaval.glo4002.reservation.domain.fullcourse.Ingredient;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.Recipe;

public class VegetarianDessert extends Recipe {
  private static final double MOZZARELLA_QUANTITY = 5;

  public VegetarianDessert() {
    ingredients.add(new Ingredient(IngredientName.MOZZARELLA, MOZZARELLA_QUANTITY));
  }
}
