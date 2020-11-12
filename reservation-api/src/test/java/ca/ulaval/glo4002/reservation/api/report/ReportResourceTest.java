package ca.ulaval.glo4002.reservation.api.report;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.presenter.material.MaterialReportPresenter;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@ExtendWith(MockitoExtension.class)
public class ReportResourceTest {

  public static final String START_DATE = "2150-07-23";
  public static final String END_DATE = "2150-07-27";
  public static final String REPORT_TYPE_STRING = "unit";
  public static final ReportType REPORT_TYPE = ReportType.UNIT;
  public static final String TOTAL_REPORT_TYPE_STRING = "total";
  public static final ReportType TOTAL_REPORT_TYPE = ReportType.TOTAL;

  @Mock
  private ReportService reportService;

  @Mock
  private ReportDateValidator reportDateValidator;

  @Mock
  private ReportPresenterFactory reportPresenterFactory;

  @Mock
  private ReportPresenter reportPresenter;

  @Mock
  private MaterialReportPresenter materialReportPresenter;

  @Mock
  private MaterialReport materialReport;

  @Mock
  private Report report;

  private ReportResource reportResource;

  @BeforeEach
  public void setUp() {
    reportResource = new ReportResource(reportService,
                                        reportDateValidator,
                                        reportPresenterFactory,
                                        materialReportPresenter);
  }

  @Test
  public void whenGetIngredientReport_thenReportIsReturnedByReportService() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getIngredientsReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportService).getIngredientReport(START_DATE, END_DATE);
  }

  @Test
  public void whenGetIngredientReport_thenDatesAreValidated() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getIngredientsReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportDateValidator).validate(START_DATE, END_DATE);
  }

  @Test
  public void whenGetIngredientReport_thenReportPresenterFactoryIsCalled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getIngredientsReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPresenterFactory).create(REPORT_TYPE);
  }

  @Test
  public void whenGetIngredientReport_thenUnitReportDtoIsAssembled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);
    given(reportService.getIngredientReport(START_DATE, END_DATE)).willReturn(report);

    // when
    reportResource.getIngredientsReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPresenter).presentReport(report);
  }

  @Test
  public void whenGetIngredientReport_thenTotalReportDtoIsAssembled() {
    // given
    given(reportPresenterFactory.create(TOTAL_REPORT_TYPE)).willReturn(reportPresenter);
    given(reportService.getIngredientReport(START_DATE, END_DATE)).willReturn(report);

    // when
    reportResource.getIngredientsReport(START_DATE, END_DATE, TOTAL_REPORT_TYPE_STRING);

    // then
    verify(reportPresenter).presentReport(report);
  }

  @Test
  public void whenGetMaterialReport_thenMaterialReportPresenterFactoryIsCalled() {
    // given
    given(reportService.getMaterialReport(START_DATE, END_DATE)).willReturn(materialReport);

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
    given(reportService.getMaterialReport(START_DATE, END_DATE)).willReturn(materialReport);

    // when
    reportResource.getMaterialReport(START_DATE, END_DATE);

    // then
    verify(materialReportPresenter).presentReport(materialReport);
  }
}
