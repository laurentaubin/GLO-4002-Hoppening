package ca.ulaval.glo4002.reservation.api.report.validator;

import java.time.LocalDate;

import ca.ulaval.glo4002.reservation.api.report.exception.InvalidReportDateException;

public class ReportDateValidator {
  private final String reportDateRegex;

  public ReportDateValidator(String reportDateRegex) {
    this.reportDateRegex = reportDateRegex;
  }

  public void validate(String startDate, String endDate) {
    if (areDatesInvalid(startDate, endDate)) {
      throw new InvalidReportDateException();
    }
  }

  private boolean areDatesInvalid(String startDate, String endDate) {
    return areDatesInValidFormat(startDate, endDate) || isStartDateAfterEndDate(startDate, endDate);
  }

  private boolean areDatesInValidFormat(String startDate, String endDate) {
    return startDate == null || endDate == null || !startDate.matches(reportDateRegex)
           || !endDate.matches(reportDateRegex);
  }

  private boolean isStartDateAfterEndDate(String startDate, String endDate) {
    return LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate));
  }
}
