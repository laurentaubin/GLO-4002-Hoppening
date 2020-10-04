package ca.ulaval.glo4002.reservation.domain.report;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.report.exception.InvalidReportDateException;

public class ReportPeriod {
  private static final LocalDate OPENING_DATE = LocalDate.of(2150, 7, 20);
  private static final LocalDate CLOSING_DATE = LocalDate.of(2150, 7, 30);

  private final LocalDate startDate;
  private final LocalDate endDate;

  public ReportPeriod(LocalDate startDate, LocalDate endDate) {
    if (!areDatesValid(startDate, endDate)) {
      throw new InvalidReportDateException();
    }
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public List<LocalDate> getAllDaysOfPeriod() {
    return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
  }

  private boolean areDatesValid(LocalDate startDate, LocalDate endDate) {
    return (isEqualOrAfterOpeningDate(startDate) && isEqualOrBeforeClosingDate(endDate)
            && isStartDateEqualOrBeforeEndDate(startDate, endDate));
  }

  private boolean isEqualOrAfterOpeningDate(LocalDate startDate) {
    return startDate.isEqual(OPENING_DATE) || startDate.isAfter(OPENING_DATE);
  }

  private boolean isEqualOrBeforeClosingDate(LocalDate endDate) {
    return endDate.isEqual(CLOSING_DATE) || endDate.isBefore(CLOSING_DATE);
  }

  private boolean isStartDateEqualOrBeforeEndDate(LocalDate startDate, LocalDate endDate) {
    return startDate.isEqual(endDate) || startDate.isBefore(endDate);
  }
}
