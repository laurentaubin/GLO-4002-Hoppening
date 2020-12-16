package ca.ulaval.glo4002.reservation.api.report.presenter.expense;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import ca.ulaval.glo4002.reservation.api.report.dto.ExpenseReportDto;
import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpenseReportDtoFactoryTest {

  private static final BigDecimal EXPENSE = BigDecimal.valueOf(2.23232432);
  private static final BigDecimal ROUNDED_EXPENSE = BigDecimal.valueOf(2.23);
  private static final BigDecimal INCOME = BigDecimal.valueOf(5.981236712);
  private static final BigDecimal ROUNDED_INCOME = BigDecimal.valueOf(5.98);
  private static final BigDecimal PROFITS = BigDecimal.valueOf(9.7263612);
  private static final BigDecimal ROUNDED_PROFITS = BigDecimal.valueOf(9.73);

  @Mock
  private ExpenseReport expenseReport;

  private ExpenseReportDtoFactory expenseReportDtoFactory;

  @BeforeEach
  public void setUp() {
    expenseReportDtoFactory = new ExpenseReportDtoFactory();
  }

  @Test
  public void givenValuesWithMultipleDecimalPlaces_whenCreate_thenRoundAtTheSecondDecimalPlace() {
    // given
    given(expenseReport.getExpense()).willReturn(EXPENSE);
    given(expenseReport.getIncome()).willReturn(INCOME);
    given(expenseReport.getProfits()).willReturn(PROFITS);

    // when
    ExpenseReportDto expenseReportDto = expenseReportDtoFactory.create(expenseReport);

    // then
    assertThat(expenseReportDto.getExpense()).isEqualTo(ROUNDED_EXPENSE);
    assertThat(expenseReportDto.getIncome()).isEqualTo(ROUNDED_INCOME);
    assertThat(expenseReportDto.getProfits()).isEqualTo(ROUNDED_PROFITS);

  }

}