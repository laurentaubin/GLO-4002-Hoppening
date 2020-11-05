package ca.ulaval.glo4002.reservation.api.report.presenter.total;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.report.Report;

@ExtendWith(MockitoExtension.class)
public class TotalReportPresenterTest {
  @Mock
  private Report report;

  @Mock
  private TotalReportDtoFactory totalReportDtoFactory;

  private TotalReportPresenter totalReportPresenter;

  @BeforeEach
  public void setUp() {
    totalReportPresenter = new TotalReportPresenter(totalReportDtoFactory);
  }

  @Test
  public void givenReport_whenPresentReport_thenTotalReportDtoIsCreated() {
    // when
    totalReportPresenter.presentReport(report);

    // then
    verify(totalReportDtoFactory).create(report);
  }
}
