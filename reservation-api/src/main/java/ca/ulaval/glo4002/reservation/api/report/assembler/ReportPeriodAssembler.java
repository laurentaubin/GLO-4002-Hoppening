package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.time.LocalDate;

import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;

public class ReportPeriodAssembler {
  public ReportPeriod assembleReportPeriod(String startDate, String endDate) {
    return new ReportPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate));
  }
}
