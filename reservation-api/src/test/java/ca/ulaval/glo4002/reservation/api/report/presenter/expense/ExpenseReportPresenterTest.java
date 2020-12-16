package ca.ulaval.glo4002.reservation.api.report.presenter.expense;

import static org.mockito.Mockito.verify;

import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExpenseReportPresenterTest {

  @Mock
  private ExpenseReport materialReport;

  @Mock
  private ExpenseReportDtoFactory materialReportDtoFactory;

  private ExpenseReportPresenter expenseReportPresenter;

  @BeforeEach
  public void setUp() {
    expenseReportPresenter = new ExpenseReportPresenter(materialReportDtoFactory);
  }

  @Test
  public void givenReport_whenPresentReport_thenTotalReportDtoIsCreated() {
    // when
    expenseReportPresenter.presentReport(materialReport);

    // then
    verify(materialReportDtoFactory).create(materialReport);
  }
}
