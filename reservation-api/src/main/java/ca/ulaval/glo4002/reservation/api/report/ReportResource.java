package ca.ulaval.glo4002.reservation.api.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@Path("/reports")
public class ReportResource {

  private final ReportService reportService;
  private final ReportDateValidator reportDateValidator;
  private final ReportPeriodAssembler reportPeriodAssembler;
  private final ReportPresenterFactory reportPresenterFactory;

  public ReportResource(ReportService reportService,
                        ReportDateValidator reportDateValidator,
                        ReportPeriodAssembler reportPeriodAssembler,
                        ReportPresenterFactory reportPresenterFactory)
  {
    this.reportService = reportService;
    this.reportDateValidator = reportDateValidator;
    this.reportPeriodAssembler = reportPeriodAssembler;
    this.reportPresenterFactory = reportPresenterFactory;
  }

  @GET
  @Path("/ingredients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReport(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate,
                            @QueryParam("type") String type)
  {
    reportDateValidator.validate(startDate, endDate);
    ReportPeriod reportPeriod = reportPeriodAssembler.assembleReportPeriod(startDate, endDate);
    Report report = reportService.getReportResponse(reportPeriod);
    ReportPresenter reportPresenter = reportPresenterFactory.create(ReportType.valueOfName(type));
    return reportPresenter.presentReport(report);
  }
}
