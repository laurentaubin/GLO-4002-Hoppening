package ca.ulaval.glo4002.reservation.domain.report;

import java.math.BigDecimal;
import java.util.Objects;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;

public class IngredientReportInformation {
  private final IngredientName ingredientName;
  private final double quantity;
  private final BigDecimal totalPrice;

  public IngredientReportInformation(IngredientName ingredientName,
                                     double quantity,
                                     BigDecimal totalPrice)
  {
    this.ingredientName = ingredientName;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  public IngredientName getIngredientName() {
    return ingredientName;
  }

  public double getQuantity() {
    return quantity;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  @Override
  public boolean equals(Object o) {

    if (o == this)
      return true;
    if (!(o instanceof IngredientReportInformation)) {
      return false;
    }
    IngredientReportInformation ingredientReportInformation = (IngredientReportInformation) o;
    return Objects.equals(ingredientName, ingredientReportInformation.ingredientName)
           && Objects.equals(quantity, ingredientReportInformation.quantity)
           && Objects.equals(totalPrice, ingredientReportInformation.totalPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingredientName, quantity, totalPrice);
  }
}
