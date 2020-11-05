package ca.ulaval.glo4002.reservation.api.report.presenter.unit;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.report.Report;

@ExtendWith(MockitoExtension.class)
public class UnitReportPresenterTest {
  @Mock
  private Report report;

  @Mock
  private UnitReportDtoFactory unitReportDtoFactory;

  private UnitReportPresenter unitReportPresenter;

  @BeforeEach
  public void setUp() {
    unitReportPresenter = new UnitReportPresenter(unitReportDtoFactory);
  }

  @Test
  public void givenReport_whenPresentReport_thenUnitReportDtoIsCreated() {
    // when
    unitReportPresenter.presentReport(report);

    // then
    verify(unitReportDtoFactory).create(report);
  }
}
