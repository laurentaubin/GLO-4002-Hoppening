package ca.ulaval.glo4002.reservation.domain.report.unit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;

public class UnitReportDay {
  private final LocalDate date;
  private final Set<IngredientReportInformation> ingredientsReportInformation;
  private final BigDecimal totalPrice;

  public UnitReportDay(LocalDate date,
                       Set<IngredientReportInformation> ingredientsReportInformation,
                       BigDecimal totalPrice)
  {
    this.date = date;
    this.ingredientsReportInformation = ingredientsReportInformation;
    this.totalPrice = totalPrice;
  }

  public Set<IngredientReportInformation> getIngredientsReportInformation() {
    return ingredientsReportInformation;
  }

  public LocalDate getDate() {
    return date;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  @Override
  public boolean equals(Object o) {

    if (o == this)
      return true;
    if (!(o instanceof UnitReportDay)) {
      return false;
    }
    UnitReportDay unitReportDay = (UnitReportDay) o;
    return Objects.equals(date, unitReportDay.date)
           && Objects.equals(totalPrice, unitReportDay.totalPrice)
           && Objects.equals(ingredientsReportInformation,
                             unitReportDay.ingredientsReportInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, ingredientsReportInformation, totalPrice);
  }

}
