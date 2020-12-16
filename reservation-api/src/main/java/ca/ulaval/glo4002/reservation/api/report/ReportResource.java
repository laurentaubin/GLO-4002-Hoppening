package ca.ulaval.glo4002.reservation.api.report;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.api.report.assembler.ChefReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.dto.ChefReportDto;
import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReport;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportType;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReport;
import ca.ulaval.glo4002.reservation.service.report.ChefReportService;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@Path("/reports")
public class ReportResource {

  private final ReportService reportService;
  private final ChefReportService chefReportService;
  private final ReportDateValidator reportDateValidator;
  private final IngredientReportPresenterFactory ingredientReportPresenterFactory;
  private final ChefReportDtoAssembler chefReportDtoAssembler;
  private final MaterialReportPresenter materialReportPresenter;

  public ReportResource(ReportService reportService,
                        ChefReportService chefReportService,
                        ReportDateValidator reportDateValidator,
                        IngredientReportPresenterFactory ingredientReportPresenterFactory,
                        ChefReportDtoAssembler chefReportDtoAssembler,
                        MaterialReportPresenter materialReportPresenter)
  {
    this.reportService = reportService;
    this.chefReportService = chefReportService;
    this.reportDateValidator = reportDateValidator;
    this.ingredientReportPresenterFactory = ingredientReportPresenterFactory;
    this.chefReportDtoAssembler = chefReportDtoAssembler;
    this.materialReportPresenter = materialReportPresenter;
  }

  @GET
  @Path("/ingredients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getIngredientReport(@QueryParam("startDate") String startDate,
                                      @QueryParam("endDate") String endDate,
                                      @QueryParam("type") String type)
  {
    reportDateValidator.validate(startDate, endDate);
    IngredientReport ingredientReport = reportService.getIngredientReport(startDate, endDate);
    IngredientReportPresenter ingredientReportPresenter = ingredientReportPresenterFactory.create(IngredientReportType.valueOfName(type));
    return ingredientReportPresenter.presentReport(ingredientReport);
  }

  @GET
  @Path("/chefs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getChefReport() {
    ChefReport chefReport = chefReportService.getChefReport();
    ChefReportDto chefReportDto = chefReportDtoAssembler.assembleChefReportDto(chefReport);
    return Response.ok().entity(chefReportDto).build();
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
