package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "quantity", "totalPrice" })
public class IngredientsReportInformationDto {
  @JsonProperty("name")
  private final String ingredientName;
  private final BigDecimal quantity;
  private final BigDecimal totalPrice;

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

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
