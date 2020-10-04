package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class UnitReportDayDto {
  private String date;
  private List<IngredientsReportInformationDto> ingredients;
  private BigDecimal totalPrice;

  public UnitReportDayDto() {
  }

  public UnitReportDayDto(LocalDate date,
                          List<IngredientsReportInformationDto> ingredients,
                          BigDecimal totalPrice)
  {
    this.date = date.toString();
    this.ingredients = ingredients;
    this.totalPrice = totalPrice;
  }

  public String getDate() {
    return date;
  }

  public void setIngredients(List<IngredientsReportInformationDto> ingredients) {
    this.ingredients = ingredients;
  }

  public List<IngredientsReportInformationDto> getIngredients() {
    return ingredients;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
