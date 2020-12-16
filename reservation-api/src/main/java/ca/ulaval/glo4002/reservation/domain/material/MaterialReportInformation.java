package ca.ulaval.glo4002.reservation.domain.material;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class MaterialReportInformation {
  private LocalDate date;
  private Map<Material, BigDecimal> cleanedDishes;
  private Map<Material, BigDecimal> boughtDishes;
  private BigDecimal totalPrice;

  public MaterialReportInformation(LocalDate date,
                                   Map<Material, BigDecimal> cleanedDishes,
                                   Map<Material, BigDecimal> boughtDishes,
                                   BigDecimal totalPrice)
  {
    this.date = date;
    this.cleanedDishes = cleanedDishes;
    this.boughtDishes = boughtDishes;
    this.totalPrice = totalPrice;
  }

  public LocalDate getDate() {
    return date;
  }

  public Map<Material, BigDecimal> getCleanedDishes() {
    return cleanedDishes;
  }

  public Map<Material, BigDecimal> getBoughtDishes() {
    return boughtDishes;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

}
