// package ca.ulaval.glo4002.reservation.service.report.assembler;
//
// import static com.google.common.truth.Truth.assertThat;
//
// import java.time.LocalDate;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
//
// import ca.ulaval.glo4002.reservation.domain.Period;
// import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
//
// public class ReportPeriodAssemblerTest {
// private static final String START_DATE = "2150-07-22";
// private static final String END_DATE = "2150-07-23";
//
// private Period dinnerPeriod = new Period(LocalDate.parse(START_DATE), LocalDate.parse(END_DATE));
//
// private ReportPeriodAssembler reportPeriodAssembler;
//
// @BeforeEach
// public void setUp() {
// reportPeriodAssembler = new ReportPeriodAssembler();
// }
//
// @Test
// public void givenDates_whenAssembleReportPeriod_thenReturnReportPeriodWithValidValues() {
// // when
// ReportPeriod reportPeriod = reportPeriodAssembler.assembleReportPeriod(START_DATE,
// END_DATE,
// dinnerPeriod);
//
// // then
// assertThat(reportPeriod.getStartDate().toString()).isEqualTo(START_DATE);
// assertThat(reportPeriod.getEndDate().toString()).isEqualTo(END_DATE);
// }
// }
//