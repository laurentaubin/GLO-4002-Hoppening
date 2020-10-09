package ca.ulaval.glo4002.reservation.domain.report.total;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;

public class TotalReport {
  private final HashSet<IngredientReportInformation> ingredientsReportInformation;
  private BigDecimal totalPrice;

  public TotalReport() {
    this.ingredientsReportInformation = new HashSet<>();
    this.totalPrice = BigDecimal.ZERO;
  }

  public Set<IngredientReportInformation> getIngredientsReportInformation() {
    return ingredientsReportInformation;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice.setScale(2, RoundingMode.HALF_UP);
  }

  public void add(IngredientReportInformation ingredient) {
    updateExistingIngredientsReportInformation(ingredient);
    this.totalPrice = this.totalPrice.add(ingredient.getTotalPrice());
  }

  private void updateExistingIngredientsReportInformation(IngredientReportInformation ingredientToAdd) {
    for (IngredientReportInformation existingIngredient : ingredientsReportInformation) {
      if (ingredientToAdd.getIngredientName().equals(existingIngredient.getIngredientName())) {
        ingredientsReportInformation.remove(existingIngredient);
        ingredientsReportInformation.add(new IngredientReportInformation(existingIngredient.getIngredientName(),
                                                                         existingIngredient.getQuantity() + ingredientToAdd.getQuantity(),
                                                                         existingIngredient.getTotalPrice()
                                                                                           .add(ingredientToAdd.getTotalPrice())));
        return;
      }
    }
    ingredientsReportInformation.add(ingredientToAdd);
  }
}
