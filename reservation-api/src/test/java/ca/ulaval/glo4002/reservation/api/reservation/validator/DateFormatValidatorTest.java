package ca.ulaval.glo4002.reservation.api.reservation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;

public class DateFormatValidatorTest {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String DATE_CORRECTLY_FORMATTED = "2150-01-21T15:23:20.142Z";
  private static final String DATE_INCORRECTLY_FORMATTED = "21-01-2150T15:23:20.142Z";

  private DateFormatValidator validator;

  @BeforeEach
  public void setup() {
    validator = new DateFormatValidator(DATE_FORMAT);
  }

  @Test
  public void givenACorrectlyFormattedDate_whenValidateFormat_thenDoesNotThrow() {
    // when
    Executable validatingRequestFormat = () -> validator.validateFormat(DATE_CORRECTLY_FORMATTED);

    // then
    assertDoesNotThrow(validatingRequestFormat);
  }

  @Test
  public void givenADateIncorrectlyFormatted_whenValidateFormat_thenThrowInvalidDateFormatException() {
    // when
    Executable validatingRequestFormat = () -> validator.validateFormat(DATE_INCORRECTLY_FORMATTED);

    // then
    assertThrows(InvalidFormatException.class, validatingRequestFormat);
  }
}
