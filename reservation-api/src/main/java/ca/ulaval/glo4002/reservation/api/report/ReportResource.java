package ca.ulaval.glo4002.reservation.api.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.UnitReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.domain.report.UnitReport;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@Path("/reports")
public class ReportResource {

  private final ReportService reportService;
  private final ReportDateValidator reportDateValidator;
  private final ReportPeriodAssembler reportPeriodAssembler;
  private final UnitReportDtoAssembler unitReportDtoAssembler;

  public ReportResource(ReportService reportService,
                        ReportDateValidator reportDateValidator,
                        ReportPeriodAssembler reportPeriodAssembler,
                        UnitReportDtoAssembler unitReportDtoAssembler)
  {
    this.reportService = reportService;
    this.reportDateValidator = reportDateValidator;
    this.reportPeriodAssembler = reportPeriodAssembler;
    this.unitReportDtoAssembler = unitReportDtoAssembler;
  }

  @GET
  @Path("/ingredients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReport(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate,
                            @QueryParam("type") String type)
  {
    reportDateValidator.validate(startDate, endDate);
    UnitReportDto unitReportDto = getUnitReportDto(startDate, endDate, type);
    return Response.ok().entity(unitReportDto).build();
  }

  private UnitReportDto getUnitReportDto(String startDate, String endDate, String type) {
    ReportPeriod reportPeriod = reportPeriodAssembler.assembleReportPeriod(startDate, endDate);
    ReportType reportType = ReportType.valueOfName(type);
    UnitReport unitReport = reportService.getUnitReport(reportPeriod, reportType);
    return unitReportDtoAssembler.assemble(unitReport);
  }
}
