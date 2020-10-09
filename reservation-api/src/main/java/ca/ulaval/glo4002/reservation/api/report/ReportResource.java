package ca.ulaval.glo4002.reservation.api.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.TotalReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.UnitReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.dto.TotalReportDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.domain.report.exception.InvalidReportTypeException;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@Path("/reports")
public class ReportResource {

  private final ReportService reportService;
  private final ReportDateValidator reportDateValidator;
  private final ReportPeriodAssembler reportPeriodAssembler;
  private final UnitReportDtoAssembler unitReportDtoAssembler;
  private final TotalReportDtoAssembler totalReportDtoAssembler;

  public ReportResource(ReportService reportService,
                        ReportDateValidator reportDateValidator,
                        ReportPeriodAssembler reportPeriodAssembler,
                        UnitReportDtoAssembler unitReportDtoAssembler,
                        TotalReportDtoAssembler totalReportDtoAssembler)
  {
    this.reportService = reportService;
    this.reportDateValidator = reportDateValidator;
    this.reportPeriodAssembler = reportPeriodAssembler;
    this.unitReportDtoAssembler = unitReportDtoAssembler;
    this.totalReportDtoAssembler = totalReportDtoAssembler;
  }

  @GET
  @Path("/ingredients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReport(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate,
                            @QueryParam("type") String type)
  {
    reportDateValidator.validate(startDate, endDate);
    ReportType reportType = ReportType.valueOfName(type);
    return getReportResponse(startDate, endDate, reportType);
  }

  private Response getReportResponse(String startDate, String endDate, ReportType reportType) {
    ReportPeriod reportPeriod = reportPeriodAssembler.assembleReportPeriod(startDate, endDate);
    if (reportType.equals(ReportType.UNIT)) {
      return getUnitReportResponse(reportPeriod);
    } else if (reportType.equals(ReportType.TOTAL)) {
      return getTotalReportResponse(reportPeriod);
    } else {
      throw new InvalidReportTypeException();
    }
  }

  private Response getUnitReportResponse(ReportPeriod reportPeriod) {
    UnitReport unitReport = reportService.getUnitReport(reportPeriod);
    UnitReportDto unitReportDto = unitReportDtoAssembler.assemble(unitReport);
    return Response.ok().entity(unitReportDto).build();
  }

  private Response getTotalReportResponse(ReportPeriod reportPeriod) {
    TotalReport totalReport = reportService.getTotalReport(reportPeriod);
    TotalReportDto totalReportDto = totalReportDtoAssembler.assemble(totalReport);
    return Response.ok().entity(totalReportDto).build();
  }
}
