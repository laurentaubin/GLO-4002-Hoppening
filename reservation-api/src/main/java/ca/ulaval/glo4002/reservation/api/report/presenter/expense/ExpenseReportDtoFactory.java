package ca.ulaval.glo4002.reservation.api.report.presenter.expense;

import ca.ulaval.glo4002.reservation.api.report.dto.ExpenseReportDto;
import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExpenseReportDtoFactory {
  public ExpenseReportDto create(ExpenseReport expenseReport) {
    BigDecimal roundedExpense = roundToSecondDecimalPlace(expenseReport.getExpense());
    BigDecimal roundedIncome = roundToSecondDecimalPlace(expenseReport.getIncome());
    BigDecimal roundedProfits = roundToSecondDecimalPlace(expenseReport.getProfits());
    return new ExpenseReportDto(roundedExpense, roundedIncome, roundedProfits);
  }

  private BigDecimal roundToSecondDecimalPlace(BigDecimal number) {
    return number.setScale(2, RoundingMode.HALF_UP);
  }
}
