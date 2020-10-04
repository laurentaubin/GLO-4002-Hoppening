package ca.ulaval.glo4002.reservation.api.report.validator;

import java.time.LocalDate;

import ca.ulaval.glo4002.reservation.api.report.exception.InvalidReportDateException;

public class ReportDateValidator {
  public void validate(String startDate, String endDate) {
    if (areDatesValid(startDate, endDate)) {
      throw new InvalidReportDateException();
    }
  }

  private boolean areDatesValid(String startDate, String endDate) {
    return areDatesNull(startDate, endDate) || isStartDateAfterEndDate(startDate, endDate);
  }

  private boolean areDatesNull(String startDate, String endDate) {
    return startDate == null || endDate == null;
  }

  private boolean isStartDateAfterEndDate(String startDate, String endDate) {
    return LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate));
  }
}
