package ca.ulaval.glo4002.reservation.domain.report;

import ca.ulaval.glo4002.reservation.domain.report.exception.InvalidReportTypeException;

public enum ReportType {
  UNIT("unit"), TOTAL("total");

  private final String reportType;

  ReportType(String reportType) {
    this.reportType = reportType;
  }

  @Override
  public String toString() {
    return reportType;
  }

  public static ReportType valueOfName(String name) {
    for (ReportType reportType : ReportType.values()) {
      if (reportType.toString().equals(name)) {
        return reportType;
      }
    }
    throw new InvalidReportTypeException();
  }
}
