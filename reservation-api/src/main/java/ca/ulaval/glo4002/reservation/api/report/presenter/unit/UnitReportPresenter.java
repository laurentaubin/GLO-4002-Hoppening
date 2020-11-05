package ca.ulaval.glo4002.reservation.api.report.presenter.unit;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;

public class UnitReportPresenter implements ReportPresenter {
  private final UnitReportDtoFactory unitReportDtoFactory;

  public UnitReportPresenter(UnitReportDtoFactory unitReportDtoFactory) {
    this.unitReportDtoFactory = unitReportDtoFactory;
  }

  public Response presentReport(Report report) {
    UnitReportDto unitReportDto = unitReportDtoFactory.create(report);
    return Response.ok().entity(unitReportDto).build();
  }
}
