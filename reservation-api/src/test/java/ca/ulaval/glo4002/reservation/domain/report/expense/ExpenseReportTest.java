package ca.ulaval.glo4002.reservation.domain.report.expense;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) class ExpenseReportTest {
  private static final BigDecimal MATERIAL_COST = BigDecimal.valueOf(12.321);
  private static final BigDecimal CHEF_COST = BigDecimal.valueOf(15.21);
  private static final BigDecimal INGREDIENT_COST = BigDecimal.valueOf(9.2133);
  private static final BigDecimal SUMMED_EXPENSE = MATERIAL_COST.add(CHEF_COST).add(INGREDIENT_COST);

  private ExpenseReport expenseReport;

  @BeforeEach
  public void setUp() {
    expenseReport = new ExpenseReport();
  }

  @Test public void whenAddExpense_thenSumAllExpensesAdded() {
    // given
    expenseReport.addExpense(MATERIAL_COST);
    expenseReport.addExpense(CHEF_COST);
    expenseReport.addExpense(INGREDIENT_COST);

    // when
    BigDecimal expense = expenseReport.getExpense();

    // then
    assertThat(expense).isEqualTo(SUMMED_EXPENSE);
  }
  @Test public void whenAddIncome_thenSumAllIncomesAdded() {
    // given
    expenseReport.addIncome(MATERIAL_COST);
    expenseReport.addIncome(CHEF_COST);
    expenseReport.addIncome(INGREDIENT_COST);

    // when
    BigDecimal expense = expenseReport.getIncome();

    // then
    assertThat(expense).isEqualTo(SUMMED_EXPENSE);
  }

  @Test
  public void givenExpenseAndNoIncome_whenGetProfit_thenReturnNegativeExpense() {
    // given
    expenseReport.addExpense(MATERIAL_COST);

    // when
    BigDecimal profits = expenseReport.getProfits();

    // then
    assertThat(profits).isEqualTo(MATERIAL_COST.negate());
  }

  @Test
  public void givenIncomeAndNoExpense_whenGetProfit_thenReturnIncome() {
    // given
    expenseReport.addIncome(MATERIAL_COST);

    // when
    BigDecimal profits = expenseReport.getProfits();

    // then
    assertThat(profits).isEqualTo(MATERIAL_COST);
  }
  @Test
  public void givenIncomeAndExpense_whenGetProfit_thenReturnIncomeSubstractedByExpenses() {
    // given
    expenseReport.addIncome(MATERIAL_COST);
    expenseReport.addExpense(CHEF_COST);

    // when
    BigDecimal profits = expenseReport.getProfits();

    // then
    assertThat(profits).isEqualTo(MATERIAL_COST.subtract(CHEF_COST));
  }
}