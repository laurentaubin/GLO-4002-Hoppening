package ca.ulaval.glo4002.reservation.domain.fullcourse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullCourse {
  private final List<Recipe> recipes;

  public FullCourse(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  public Map<IngredientName, Double> getIngredientsQuantity() {
    Map<IngredientName, Double> ingredientsQuantity = new HashMap<>();
    for (Recipe recipe : recipes) {
      ingredientsQuantity = updateIngredientsQuantity(ingredientsQuantity, recipe.getIngredients());
    }
    return ingredientsQuantity;
  }

  private Map<IngredientName, Double> updateIngredientsQuantity(Map<IngredientName, Double> ingredientsQuantity,
                                                                List<Ingredient> ingredients)
  {
    Map<IngredientName, Double> updatedTotalIngredientsQuantity = new HashMap<>(ingredientsQuantity);
    for (Ingredient ingredient : ingredients) {
      if (updatedTotalIngredientsQuantity.containsKey(ingredient.getIngredient())) {
        updatedTotalIngredientsQuantity.replace(ingredient.getIngredient(),
                                                updatedTotalIngredientsQuantity.get(ingredient.getIngredient())
                                                                            + ingredient.getQuantity());
      } else {
        updatedTotalIngredientsQuantity.put(ingredient.getIngredient(), ingredient.getQuantity());
      }
    }
    return updatedTotalIngredientsQuantity;
  }
}
