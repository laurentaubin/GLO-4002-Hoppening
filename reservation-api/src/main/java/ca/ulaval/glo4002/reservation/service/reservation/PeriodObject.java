package ca.ulaval.glo4002.reservation.service.reservation;

public class PeriodObject {
  private final String startDate;
  private final String endDate;

  public PeriodObject(String startDate, String endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }
}
