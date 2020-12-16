package ca.ulaval.glo4002.reservation.domain.report.expense;

import java.math.BigDecimal;

public class ExpenseReport {
  private BigDecimal expense = BigDecimal.ZERO;
  private BigDecimal income = BigDecimal.ZERO;

  public void addExpense(BigDecimal newExpense) {
    expense = expense.add(newExpense);
  }

  public void addIncome(BigDecimal newIncome) {
    income = income.add(newIncome);
  }

  public BigDecimal getExpense() {
    return expense;
  }

  public BigDecimal getIncome() {
    return income;
  }

  public BigDecimal getProfits() {
    return income.subtract(expense);
  }

}
