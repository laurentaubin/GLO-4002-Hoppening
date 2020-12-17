package ca.ulaval.glo4002.reservation.domain.material;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaterialReportTest {
  private static final BigDecimal A_COST = BigDecimal.TEN;

  @Mock
  private MaterialReportInformation materialReportInformation;


  @Test
  public void givenMaterialReportInformation_whenCalculateTotalCost_thenReturnSumOfInformationCost() {
    // given
    List<MaterialReportInformation> materialReportInformations = Collections.singletonList(materialReportInformation);
    given(materialReportInformation.getTotalPrice()).willReturn(A_COST);
    MaterialReport materialReport = new MaterialReport(materialReportInformations);

    // when
    BigDecimal totalCost = materialReport.calculateTotalCost();

    // then
    assertThat(totalCost).isEqualTo(A_COST);
  }

}