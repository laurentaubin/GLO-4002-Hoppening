package ca.ulaval.glo4002.reservation.api.report.presenter.expense;

import ca.ulaval.glo4002.reservation.api.report.dto.ExpenseReportDto;
import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
import javax.ws.rs.core.Response;

public class ExpenseReportPresenter {
  private final ExpenseReportDtoFactory expenseReportDtoFactory;

  public ExpenseReportPresenter(ExpenseReportDtoFactory expenseReportDtoFactory) {
    this.expenseReportDtoFactory = expenseReportDtoFactory;
  }

  public Response presentReport(ExpenseReport materialReport) {
    ExpenseReportDto expenseReportDto = expenseReportDtoFactory.create(materialReport);
    return Response.ok().entity(expenseReportDto).build();

  }
}
