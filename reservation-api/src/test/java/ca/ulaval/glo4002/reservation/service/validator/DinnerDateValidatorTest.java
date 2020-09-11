package ca.ulaval.glo4002.reservation.service.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;
import ca.ulaval.glo4002.reservation.service.exception.InvalidDinnerDateException;

public class DinnerDateValidatorTest {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String A_VALID_DATE = "2150-07-21T15:23:20.142Z";
  private static final String A_INVALID_DATE = "21-05-30";
  private static final String BEFORE_OPENING_DATE = "2150-01-21T15:23:20.142Z";
  private static final String OPENING_DATE = "2150-07-20T00:00:00.000Z";
  private static final String CLOSING_DATE = "2150-07-30T23:59:59.999Z";
  private static final String AFTER_CLOSING_DATE = "2150-08-30T23:59:59.998Z";

  private DinnerDateValidator dinnerDateValidator;

  @BeforeEach
  public void setup() {
    dinnerDateValidator = new DinnerDateValidator(DATE_FORMAT, OPENING_DATE, CLOSING_DATE);
  }

  @Test
  public void givenADateInAValidFormat_whenValidateDate_thenDoesNotThrow() {
    // when
    Executable validatingDateFormat = () -> dinnerDateValidator.validateDate(A_VALID_DATE);

    // then
    assertDoesNotThrow(validatingDateFormat);
  }

  @Test
  public void givenADateInAnInvalidFormat_whenValidateDate_thenThrowInvalidDateFormatException() {
    // when
    Executable validatingDateFormat = () -> dinnerDateValidator.validateDate(A_INVALID_DATE);

    // then
    assertThrows(InvalidFormatException.class, validatingDateFormat);
  }

  @Test
  public void givenDateBeforeOpening_whenValidateDate_thenThrowInvalidDateRange() {
    // when
    Executable validatingDateRange = () -> dinnerDateValidator.validateDate(BEFORE_OPENING_DATE);

    // then
    assertThrows(InvalidDinnerDateException.class, validatingDateRange);
  }

  @Test
  public void givenDateTheSameDayAsTheOpeningDay_whenValidatingDate_thenDoNotThrow() {
    // when
    Executable validatingDateRage = () -> dinnerDateValidator.validateDate(OPENING_DATE);

    // then
    assertDoesNotThrow(validatingDateRage);
  }

  @Test
  public void givenDateAfterClosing_whenValidateDate_thenThrowInvalidDateRange() {
    // when
    Executable validatingDateRange = () -> dinnerDateValidator.validateDate(AFTER_CLOSING_DATE);

    // then
    assertThrows(InvalidDinnerDateException.class, validatingDateRange);
  }

  @Test
  public void givenDateTheSameDayAsTheClosingDay_whenValidateDate_thenDoNotThrow() {
    // when
    Executable validatingDateRage = () -> dinnerDateValidator.validateDate(CLOSING_DATE);

    // then
    assertDoesNotThrow(validatingDateRage);
  }
}
