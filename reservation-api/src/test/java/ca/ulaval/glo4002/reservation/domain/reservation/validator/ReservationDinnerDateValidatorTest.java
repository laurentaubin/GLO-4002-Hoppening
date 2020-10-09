package ca.ulaval.glo4002.reservation.domain.reservation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationDateException;

public class ReservationDinnerDateValidatorTest {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String STARTING_DATE = "2150-02-01T00:00:00.000Z";
  private static final String ENDING_DATE = "2150-07-16T23:59:59.999Z";
  private static final String BEFORE_STARTING_DATE = "2150-01-16T23:59:59.999Z";
  private static final String AFTER_ENDING_DATE = "2150-11-16T23:59:59.999Z";

  private ReservationDateValidator reservationDateValidator;

  @BeforeEach
  public void setup() {
    reservationDateValidator = new ReservationDateValidator(DATE_FORMAT,
                                                            STARTING_DATE,
                                                            ENDING_DATE);
  }

  @Test
  public void givenDateBeforeStarting_whenValidateDate_thenThrowInvalidDateRange() {
    // when
    Executable validatingDateRange = () -> reservationDateValidator.validate(BEFORE_STARTING_DATE);

    // then
    assertThrows(InvalidReservationDateException.class, validatingDateRange);
  }

  @Test
  public void givenDateTheSameDayAsTheStartingDay_whenValidatingDate_thenDoNotThrow() {
    // when
    Executable validatingDateRage = () -> reservationDateValidator.validate(STARTING_DATE);

    // then
    assertDoesNotThrow(validatingDateRage);
  }

  @Test
  public void givenDateAfterEnding_whenValidateDate_thenThrowInvalidDateRange() {
    // when
    Executable validatingDateRange = () -> reservationDateValidator.validate(AFTER_ENDING_DATE);

    // then
    assertThrows(InvalidReservationDateException.class, validatingDateRange);
  }

  @Test
  public void givenDateTheSameDayAsTheEndingDay_whenValidateDate_thenDoNotThrow() {
    // when
    Executable validatingDateRage = () -> reservationDateValidator.validate(ENDING_DATE);

    // then
    assertDoesNotThrow(validatingDateRage);
  }

}
