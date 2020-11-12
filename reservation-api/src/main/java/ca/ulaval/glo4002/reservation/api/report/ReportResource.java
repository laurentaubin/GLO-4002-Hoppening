package ca.ulaval.glo4002.reservation.api.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@Path("/reports")
public class ReportResource {

  private final ReportService reportService;
  private final ReportDateValidator reportDateValidator;
  private final ReportPresenterFactory reportPresenterFactory;
  private final MaterialReportPresenter materialReportPresenter;

  public ReportResource(ReportService reportService,
                        ReportDateValidator reportDateValidator,
                        ReportPresenterFactory reportPresenterFactory,
                        MaterialReportPresenter materialReportPresenter)
  {
    this.reportService = reportService;
    this.reportDateValidator = reportDateValidator;
    this.reportPresenterFactory = reportPresenterFactory;
    this.materialReportPresenter = materialReportPresenter;
  }

  @GET
  @Path("/ingredients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getIngredientsReport(@QueryParam("startDate") String startDate,
                                       @QueryParam("endDate") String endDate,
                                       @QueryParam("type") String type)
  {
    reportDateValidator.validate(startDate, endDate);
    Report report = reportService.getIngredientReport(startDate, endDate);

    ReportPresenter reportPresenter = reportPresenterFactory.create(ReportType.valueOfName(type));
    return reportPresenter.presentReport(report);
  }

  @GET
  @Path("/material")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMaterialReport(@QueryParam("startDate") String startDate,
                                    @QueryParam("endDate") String endDate)
  {
    reportDateValidator.validate(startDate, endDate);
    MaterialReport materialReport = reportService.getMaterialReport(startDate, endDate);
    return materialReportPresenter.presentReport(materialReport);
  }
}
