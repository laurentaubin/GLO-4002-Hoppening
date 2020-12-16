package ca.ulaval.glo4002.reservation.api.report.dto;

import java.math.BigDecimal;

public class ExpenseReportDto {

  private final BigDecimal expense;
  private final BigDecimal income;
  private final BigDecimal profits;

  public ExpenseReportDto(BigDecimal expense, BigDecimal income, BigDecimal profits) {
    this.expense = expense;
    this.income = income;
    this.profits = profits;
  }

  public BigDecimal getExpense() {
    return expense;
  }

  public BigDecimal getIncome() {
    return income;
  }

  public BigDecimal getProfits() {
    return profits;
  }
}
