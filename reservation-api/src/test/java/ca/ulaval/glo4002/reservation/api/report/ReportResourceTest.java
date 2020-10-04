package ca.ulaval.glo4002.reservation.api.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.assembler.ReportPeriodAssembler;
import ca.ulaval.glo4002.reservation.api.report.assembler.UnitReportDtoAssembler;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.domain.report.UnitReport;
import ca.ulaval.glo4002.reservation.service.report.ReportService;

@ExtendWith(MockitoExtension.class)
public class ReportResourceTest {
  public static final String START_DATE = "2150-07-23";
  public static final String END_DATE = "2150-07-27";
  public static final String REPORT_TYPE_STRING = "unit";
  public static final ReportType REPORT_TYPE = ReportType.UNIT;

  @Mock
  private ReportService reportService;

  @Mock
  private ReportDateValidator reportDateValidator;

  @Mock
  private ReportPeriodAssembler reportPeriodAssembler;

  @Mock
  private UnitReportDtoAssembler unitReportDtoAssembler;

  @Mock
  private UnitReport unitReport;

  @Mock
  private ReportPeriod reportPeriod;

  private ReportResource reportResource;

  @BeforeEach
  public void setUp() {
    reportResource = new ReportResource(reportService,
                                        reportDateValidator,
                                        reportPeriodAssembler,
                                        unitReportDtoAssembler);
  }

  @Test
  public void givenStartDateEndDateAndReportType_whenGetReport_thenReportIsReturnedByReportService() {
    // given
    given(reportPeriodAssembler.assembleReportPeriod(START_DATE,
                                                     END_DATE)).willReturn(reportPeriod);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportService).getUnitReport(reportPeriod, REPORT_TYPE);
  }

  @Test
  public void whenGetReport_thenDatesAreValidated() {
    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportDateValidator).validate(START_DATE, END_DATE);
  }

  @Test
  public void whenGetReport_thenReportPeriodIsAssembled() {
    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(reportPeriodAssembler).assembleReportPeriod(START_DATE, END_DATE);
  }

  @Test
  public void whenGetReport_thenUnitReportDtoIsAssembled() {
    // given
    given(reportService.getUnitReport(any(), any())).willReturn(unitReport);

    // when
    reportResource.getReport(START_DATE, END_DATE, REPORT_TYPE_STRING);

    // then
    verify(unitReportDtoAssembler).assemble(unitReport);
  }
}
