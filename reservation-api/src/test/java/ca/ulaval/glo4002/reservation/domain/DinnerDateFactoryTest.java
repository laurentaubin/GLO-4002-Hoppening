package ca.ulaval.glo4002.reservation.domain;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidDinnerDateException;

@ExtendWith(MockitoExtension.class)
public class DinnerDateFactoryTest {
  private static final String A_DATE = "2150-07-30T22:59:59.000Z";
  private static final LocalDateTime A_DATE_TO_LOCAL_DATE_TIME = LocalDateTime.of(2150,
                                                                                  7,
                                                                                  30,
                                                                                  22,
                                                                                  59,
                                                                                  59,
                                                                                  0);
  private static final LocalDate START_DATE = LocalDate.of(2150, 7, 20);
  private static final LocalDate END_DATE = LocalDate.of(2150, 7, 30);

  @Mock
  private Period period;

  private DinnerDateFactory dinnerDateFactory;

  @BeforeEach
  public void setUpDinnerDateFactory() {
    dinnerDateFactory = new DinnerDateFactory();
  }

  @Test
  public void givenADateInPeriod_whenCreate_thenDinnerDateIsCreated() {
    // given
    given(period.isWithinPeriod(A_DATE_TO_LOCAL_DATE_TIME.toLocalDate())).willReturn(true);

    // when
    DinnerDate dinnerDate = dinnerDateFactory.create(A_DATE, period);

    // then
    assertThat(dinnerDate.getLocalDateTime()).isEqualTo(A_DATE_TO_LOCAL_DATE_TIME);
  }

  @Test
  public void givenADateNotInPeriod_whenCreate_thenThrowInvalidDinnerDateException() {
    // given
    given(period.isWithinPeriod(A_DATE_TO_LOCAL_DATE_TIME.toLocalDate())).willReturn(false);
    given(period.getStartDate()).willReturn(START_DATE);
    given(period.getEndDate()).willReturn(END_DATE);
    // when
    Executable validatingDateRange = () -> dinnerDateFactory.create(A_DATE, period);

    // then
    assertThrows(InvalidDinnerDateException.class, validatingDateRange);
  }
}
