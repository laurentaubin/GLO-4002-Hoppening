package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "ingredients", "totalPrice" })
public class TotalReportDto {
  @JsonProperty("ingredients")
  private List<IngredientsReportInformationDto> ingredients;
  private BigDecimal totalPrice;

  public TotalReportDto(List<IngredientsReportInformationDto> ingredients, BigDecimal totalPrice) {
    this.ingredients = ingredients;
    this.totalPrice = totalPrice;
  }

  public List<IngredientsReportInformationDto> getIngredients() {
    return ingredients;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
