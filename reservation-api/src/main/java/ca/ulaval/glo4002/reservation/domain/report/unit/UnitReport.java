package ca.ulaval.glo4002.reservation.domain.report.unit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnitReport {
  private List<UnitReportDay> unitReportDays;
  private BigDecimal totalPrice;

  public UnitReport() {
    unitReportDays = new ArrayList<>();
  }

  public UnitReport(List<UnitReportDay> unitReportDays, BigDecimal totalPrice) {
    this.unitReportDays = unitReportDays;
    this.totalPrice = totalPrice;
  }

  public boolean isEmpty() {
    return unitReportDays.isEmpty();
  }

  public List<UnitReportDay> getUnitReportDays() {
    return unitReportDays;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void add(UnitReportDay unitReportDay) {
    unitReportDays.add(unitReportDay);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof UnitReport)) {
      return false;
    }
    UnitReport unitReport = (UnitReport) o;
    return Objects.equals(unitReportDays, unitReport.unitReportDays)
           && Objects.equals(totalPrice, unitReport.totalPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unitReportDays, totalPrice);
  }
}
