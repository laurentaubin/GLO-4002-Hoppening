package ca.ulaval.glo4002.reservation.domain.fullcourse;

public class Ingredient {
  private final IngredientName ingredientName;
  private final double quantity;

  public Ingredient(IngredientName ingredientName, double quantity) {
    this.ingredientName = ingredientName;
    this.quantity = quantity;
  }

  public IngredientName getIngredient() {
    return ingredientName;
  }

  public double getQuantity() {
    return quantity;
  }
}
