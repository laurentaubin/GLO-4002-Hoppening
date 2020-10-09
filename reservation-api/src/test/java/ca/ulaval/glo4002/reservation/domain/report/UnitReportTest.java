package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportDay;

@ExtendWith(MockitoExtension.class)
public class UnitReportTest {

  @Mock
  private UnitReportDay aUnitReportDay;

  @Test
  public void whenCreateUnitReport_thenReportIsEmpty() {
    // when
    UnitReport unitReport = new UnitReport();

    // then
    assertThat(unitReport.isEmpty()).isTrue();
  }

  @Test
  public void givenUnitReportLine_whenAdd_thenUnitReportLineIsAddedToUnitReport() {
    // given
    UnitReport unitReport = new UnitReport();

    // when
    unitReport.add(aUnitReportDay);

    // then
    assertThat(unitReport.getUnitReportDays()).contains(aUnitReportDay);
  }
}
