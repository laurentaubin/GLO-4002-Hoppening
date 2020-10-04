package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;

public class ReportPeriodAssemblerTest {
  private static final String START_DATE = "2150-07-22";
  private static final String END_DATE = "2150-07-23";

  private ReportPeriodAssembler reportPeriodAssembler;

  @BeforeEach
  public void setUp() {
    reportPeriodAssembler = new ReportPeriodAssembler();
  }

  @Test
  public void givenDates_whenAssembleReportPeriod_thenReturnReportPeriodWithValidValues() {
    // when
    ReportPeriod reportPeriod = reportPeriodAssembler.assembleReportPeriod(START_DATE, END_DATE);

    // then
    assertThat(reportPeriod.getStartDate().toString()).isEqualTo(START_DATE);
    assertThat(reportPeriod.getEndDate().toString()).isEqualTo(END_DATE);
  }
}
