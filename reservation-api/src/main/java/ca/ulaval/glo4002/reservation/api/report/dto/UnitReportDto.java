package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "dates", "totalPrice" })
public class UnitReportDto {
  @JsonProperty("dates")
  private List<UnitReportDayDto> unitReportDayDtos;
  private BigDecimal totalPrice;

  public UnitReportDto() {
  }

  public UnitReportDto(List<UnitReportDayDto> unitReportDayDtos, BigDecimal totalPrice) {
    this.unitReportDayDtos = unitReportDayDtos;
    this.totalPrice = totalPrice;
  }

  public List<UnitReportDayDto> getUnitReportDayDtos() {
    return unitReportDayDtos;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
