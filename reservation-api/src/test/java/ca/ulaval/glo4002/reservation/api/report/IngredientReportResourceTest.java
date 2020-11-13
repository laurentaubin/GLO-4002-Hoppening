package ca.ulaval.glo4002.reservation.api.report;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.assembler.ChefReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReport;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportType;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.service.report.ChefReportService;
import ca.ulaval.glo4002.reservation.service.report.IngredientReportService;

@ExtendWith(MockitoExtension.class)
public class IngredientReportResourceTest {
  public static final String START_DATE = "2150-07-23";
  public static final String END_DATE = "2150-07-27";
  public static final String REPORT_TYPE_STRING = "unit";
  public static final IngredientReportType REPORT_TYPE = IngredientReportType.UNIT;
  public static final String TOTAL_REPORT_TYPE_STRING = "total";
  public static final IngredientReportType TOTAL_REPORT_TYPE = IngredientReportType.TOTAL;

  @Mock
  private IngredientReportService ingredientReportService;

  @Mock
  private ChefReportService chefReportService;

  @Mock
  private ReportDateValidator reportDateValidator;

  @Mock
  private IngredientReport ingredientReport;

  @Mock
  private MaterialReport materialReport;

  @Mock
  private ReportPeriod reportPeriod;

  @Mock
  private ReportPresenterFactory reportPresenterFactory;

  @Mock
  private IngredientReportPresenter ingredientReportPresenter;

  @Mock
  private ChefReportDtoAssembler chefReportDtoAssembler;

  @Mock
  private MaterialReportPresenter materialReportPresenter;

  private ReportResource reportResource;

  @BeforeEach
  public void setUp() {
    reportResource =
        new ReportResource(ingredientReportService, chefReportService, reportDateValidator,
            reportPresenterFactory, chefReportDtoAssembler, materialReportPresenter);
  }

  @Test
  public void whenGetIngredientReport_thenReportIsReturnedByReportService() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(ingredientReportPresenter);

    // when
    reportResource.getIngredientReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(ingredientReportService).getIngredientReport(START_DATE, END_DATE);
  }

  @Test
  public void whenGetIngredientReport_thenDatesAreValidated() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(ingredientReportPresenter);

    // when
    reportResource.getIngredientReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportDateValidator).validate(START_DATE, END_DATE);
  }

  @Test
  public void whenGetIngredientReport_thenReportPresenterFactoryIsCalled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(ingredientReportPresenter);

    // when
    reportResource.getIngredientReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPresenterFactory).create(REPORT_TYPE);
  }

  @Test
  public void whenGetIngredientReport_thenUnitReportDtoIsAssembled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(ingredientReportPresenter);
    given(ingredientReportService.getIngredientReport(START_DATE, END_DATE))
        .willReturn(ingredientReport);

    // when
    reportResource.getIngredientReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(ingredientReportPresenter).presentReport(ingredientReport);
  }

  @Test
  public void whenGetIngredientReport_thenTotalReportDtoIsAssembled() {
    // given
    given(reportPresenterFactory.create(TOTAL_REPORT_TYPE)).willReturn(ingredientReportPresenter);
    given(ingredientReportService.getIngredientReport(START_DATE, END_DATE))
        .willReturn(ingredientReport);

    // when
    reportResource.getIngredientReport(START_DATE, END_DATE, TOTAL_REPORT_TYPE_STRING);

    // then
    verify(ingredientReportPresenter).presentReport(ingredientReport);
  }

  @Test
  public void whenGetMaterialReport_thenMaterialReportPresenterFactoryIsCalled() {
    // given
    given(ingredientReportService.getMaterialReport(START_DATE, END_DATE))
        .willReturn(materialReport);

    // when
    reportResource.getMaterialReport(START_DATE, END_DATE);

    // then
    verify(materialReportPresenter).presentReport(materialReport);
  }

  @Test
  public void whenGetMaterialReport_thenDatesAreValidated() {
    // when
    reportResource.getMaterialReport(START_DATE, END_DATE);

    // then
    verify(reportDateValidator).validate(START_DATE, END_DATE);
  }

  @Test
  public void whenGetMaterialReport_thenMaterialReportDtoIsAssembled() {
    // given
    given(ingredientReportService.getMaterialReport(START_DATE, END_DATE))
        .willReturn(materialReport);

    // when
    reportResource.getMaterialReport(START_DATE, END_DATE);

    // then
    verify(materialReportPresenter).presentReport(materialReport);
  }
}
