package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.report.exception.InvalidReportDateException;

public class ReportPeriodTest {
  private static final LocalDate VALID_START_DATE = LocalDate.of(2150, 7, 21);
  private static final LocalDate VALID_END_DATE = LocalDate.of(2150, 7, 23);
  private static final LocalDate BEFORE_OPENING_DATE = LocalDate.of(2150, 6, 29);
  private static final LocalDate AFTER_CLOSING_DATE = LocalDate.of(2150, 8, 1);
  private static final LocalDate OPENING_DATE = LocalDate.of(2150, 7, 20);
  private static final LocalDate CLOSING_DATE = LocalDate.of(2150, 7, 30);

  @Test
  public void givenValidStartAndEndDate_whenInitialized_thenReportPeriodIsConstructed() {
    // when
    ReportPeriod reportPeriod = new ReportPeriod(VALID_START_DATE, VALID_END_DATE);

    // then
    assertThat(reportPeriod.getStartDate()).isEqualTo(VALID_START_DATE);
    assertThat(reportPeriod.getEndDate()).isEqualTo(VALID_END_DATE);
  }

  @Test
  public void givenStartDateBeingBeforeOpeningDate_whenInitialized_thenThrowInvalidReportDateException() {
    // when
    Executable initializingReportPeriod = () -> new ReportPeriod(BEFORE_OPENING_DATE,
                                                                 VALID_END_DATE);

    // then
    assertThrows(InvalidReportDateException.class, initializingReportPeriod);
  }

  @Test
  public void givenEndDateBeingAfterClosingDate_whenInitialized_thenThrowInvalidReportDateException() {
    // when
    Executable initializingReportPeriod = () -> new ReportPeriod(VALID_START_DATE,
                                                                 AFTER_CLOSING_DATE);

    // then
    assertThrows(InvalidReportDateException.class, initializingReportPeriod);
  }

  @Test
  public void whenGetDatesBetween_thenReturnedAllTheDaysBetweenStartDateAndEndDate() {
    // given
    ReportPeriod reportPeriod = new ReportPeriod(VALID_START_DATE, VALID_END_DATE);
    LocalDate expectedInBetweenDate = LocalDate.of(2150, 7, 22);

    // when
    List<LocalDate> days = reportPeriod.getAllDaysOfPeriod();

    // then
    assertThat(days.contains(VALID_START_DATE)).isTrue();
    assertThat(days.contains(expectedInBetweenDate)).isTrue();
    assertThat(days.contains(VALID_END_DATE)).isTrue();
  }

  @Test
  public void givenStartDateBeingTheSameDateAsOpeningDate_whenInitialized_thenReportPeriodIsCreated() {
    // when
    Executable initializingReportPeriod = () -> new ReportPeriod(OPENING_DATE, VALID_END_DATE);

    // then
    assertDoesNotThrow(initializingReportPeriod);
  }

  @Test
  public void givenEndDateBeingTheSameDateAsClosingDate_whenInitialized_thenReportPeriodIsCreated() {
    // when
    Executable initializingReportPeriod = () -> new ReportPeriod(VALID_START_DATE, CLOSING_DATE);

    // then
    assertDoesNotThrow(initializingReportPeriod);
  }

  @Test
  public void givenTheSameDates_whenGetDatesBetween_thenOnlyReturnedTheDate() {
    // given
    ReportPeriod reportPeriod = new ReportPeriod(VALID_START_DATE, VALID_START_DATE);

    // when
    List<LocalDate> days = reportPeriod.getAllDaysOfPeriod();

    // then
    assertThat(days.size()).isEqualTo(1);
    assertThat(days.contains(VALID_START_DATE)).isTrue();
  }
}
