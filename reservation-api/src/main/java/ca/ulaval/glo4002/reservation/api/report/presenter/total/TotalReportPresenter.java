package ca.ulaval.glo4002.reservation.api.report.presenter.total;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.dto.TotalReportDto;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;

public class TotalReportPresenter implements ReportPresenter {
  private final TotalReportDtoFactory totalReportDtoFactory;

  public TotalReportPresenter(TotalReportDtoFactory totalReportDtoFactory) {
    this.totalReportDtoFactory = totalReportDtoFactory;
  }

  public Response presentReport(Report report) {
    TotalReportDto totalReportDto = totalReportDtoFactory.create(report);
    return Response.ok().entity(totalReportDto).build();
  }
}
