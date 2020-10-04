package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;

public class IngredientsReportInformationDto {
  private String ingredientName;
  private BigDecimal quantity;
  private BigDecimal totalPrice;

  public IngredientsReportInformationDto() {
  }

  public IngredientsReportInformationDto(String ingredientName,
                                         BigDecimal quantity,
                                         BigDecimal totalPrice)
  {
    this.ingredientName = ingredientName;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  public String getIngredientName() {
    return ingredientName;
  }

  public void setIngredientName(String ingredientName) {
    this.ingredientName = ingredientName;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
