package ca.ulaval.glo4002.reservation.api.report;

import ca.ulaval.glo4002.reservation.api.report.presenter.total.TotalReportDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.presenter.total.TotalReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.presenter.unit.UnitReportDtoFactory;
import ca.ulaval.glo4002.reservation.api.report.presenter.unit.UnitReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;

public class ReportPresenterFactory {
  private final UnitReportDtoFactory unitReportDtoFactory;
  private final TotalReportDtoFactory totalReportDtoFactory;

  public ReportPresenterFactory(UnitReportDtoFactory unitReportDtoFactory,
                                TotalReportDtoFactory totalReportDtoFactory)
  {
    this.unitReportDtoFactory = unitReportDtoFactory;
    this.totalReportDtoFactory = totalReportDtoFactory;
  }

  public ReportPresenter create(ReportType reportType) {
    if (reportType.equals(ReportType.UNIT)) {
      return new UnitReportPresenter(unitReportDtoFactory);
    } else {
      return new TotalReportPresenter(totalReportDtoFactory);
    }
  }
}
