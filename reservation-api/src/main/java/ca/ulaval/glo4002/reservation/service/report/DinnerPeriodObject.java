package ca.ulaval.glo4002.reservation.service.report;

import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.domain.Period;

public class DinnerPeriodObject {
  private static final String PATTERN_FORMAT = "MMMM dd YYYY";

  private final String startDate;
  private final String endDate;

  public DinnerPeriodObject(Period dinnerPeriod) {
    this.startDate = dinnerPeriod.getStartDate()
                                 .format(DateTimeFormatter.ofPattern(PATTERN_FORMAT));
    this.endDate = dinnerPeriod.getEndDate().format(DateTimeFormatter.ofPattern(PATTERN_FORMAT));
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

}
