package ca.ulaval.glo4002.reservation.api.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportPresenter;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
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
  private ReportPeriodAssembler reportPeriodAssembler;

  @Mock
  private Report report;

  @Mock
  private ReportPeriod reportPeriod;

  @Mock
  private ReportPresenterFactory reportPresenterFactory;

  @Mock
  private ReportPresenter reportPresenter;

  private ReportResource reportResource;

  @BeforeEach
  public void setUp() {
    reportResource = new ReportResource(reportService,
                                        reportDateValidator,
                                        reportPeriodAssembler,
                                        reportPresenterFactory);
  }

  @Test
  public void givenStartDateEndDateAndReportType_whenGetReport_thenReportIsReturnedByReportService() {
    // given
    given(reportPeriodAssembler.assembleReportPeriod(START_DATE,
                                                     END_DATE)).willReturn(reportPeriod);
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportService).getReportResponse(reportPeriod);
  }

  @Test
  public void whenGetReport_thenDatesAreValidated() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportDateValidator).validate(START_DATE, END_DATE);
  }

  @Test
  public void whenGetReport_thenReportPeriodIsAssembled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPeriodAssembler).assembleReportPeriod(START_DATE, END_DATE);
  }

  @Test
  public void whenGetReport_thenReportPresenterFactoryIsCalled() {
    // given
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPresenterFactory).create(REPORT_TYPE);
  }

  @Test
  public void whenGetReport_thenUnitReportDtoIsAssembled() {
    // given
    given(reportPeriodAssembler.assembleReportPeriod(START_DATE,
            END_DATE)).willReturn(reportPeriod);
    given(reportPresenterFactory.create(REPORT_TYPE)).willReturn(reportPresenter);
    given(reportService.getReportResponse(reportPeriod)).willReturn(report);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPresenter).presentReport(report);
  }

  @Test
  public void whenGetReport_thenTotalReportDtoIsAssembled() {
    // given
    given(reportPeriodAssembler.assembleReportPeriod(START_DATE,
            END_DATE)).willReturn(reportPeriod);
    given(reportPresenterFactory.create(TOTAL_REPORT_TYPE)).willReturn(reportPresenter);
    given(reportService.getReportResponse(reportPeriod)).willReturn(report);

    // when
    reportResource.getReport(START_DATE, END_DATE, TOTAL_REPORT_TYPE_STRING);

    // then
    verify(reportPresenter).presentReport(report);
  }
}
