package ca.ulaval.glo4002.reservation.api.report;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.report.exception.InvalidReportDateException;
import ca.ulaval.glo4002.reservation.api.report.validator.ReportDateValidator;

public class ReportDateValidatorTest {

  private static final String VALID_START_DATE = "2150-05-09";
  private static final String VALID_END_DATE = "2150-06-09";
  private static final String NULL_START_DATE = null;
  private static final String NULL_END_DATE = null;
  private static final String AFTER_START_DATE = "2150-05-11";
  private static final String BEFORE_END_DATE = "2150-05-09";
  private static final String SAME_DATE = "2150-05-05";

  private ReportDateValidator reportDateValidator;

  @BeforeEach
  public void setUp() {
    reportDateValidator = new ReportDateValidator();
  }

  @Test
  public void givenValidStartAndEndDates_whenValidate_thenReportDatesAreValid() {
    // when
    Executable validatingDates = () -> reportDateValidator.validate(VALID_START_DATE,
                                                                    VALID_END_DATE);

    // then
    assertDoesNotThrow(validatingDates);
  }

  @Test
  public void givenNullStartDate_whenValidate_thenThrowInvalidReportDateException() {
    // when
    Executable validatingDates = () -> reportDateValidator.validate(NULL_START_DATE,
                                                                    VALID_END_DATE);

    // then
    assertThrows(InvalidReportDateException.class, validatingDates);
  }

  @Test
  public void givenNullEndDate_whenValidate_thenThrowInvalidReportDateException() {
    // when
    Executable validatingDates = () -> reportDateValidator.validate(VALID_START_DATE,
                                                                    NULL_END_DATE);

    // then
    assertThrows(InvalidReportDateException.class, validatingDates);
  }

  @Test
  public void givenEndDateBeingBeforeStartDate_whenValidate_thenThrowInvalidReportDateException() {
    // when
    Executable validatingDates = () -> reportDateValidator.validate(AFTER_START_DATE,
                                                                    BEFORE_END_DATE);

    // then
    assertThrows(InvalidReportDateException.class, validatingDates);
  }

  @Test
  public void givenStartAndEndDateBeingTheSameDate_whenValidate_thenDatesAreValid() {
    // when
    Executable validatingDates = () -> reportDateValidator.validate(SAME_DATE, SAME_DATE);

    // then
    assertDoesNotThrow(validatingDates);
  }
}
