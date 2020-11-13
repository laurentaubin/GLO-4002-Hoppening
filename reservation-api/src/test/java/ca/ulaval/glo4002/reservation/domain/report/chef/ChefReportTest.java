package ca.ulaval.glo4002.reservation.domain.report.chef;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefType;

class ChefReportTest {

  private static final Set<Chef> SOME_CHEFS = Set.of(new Chef(ChefType.THIERRY_AKI));
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;

  @Test
  public void whenAddingReportInformation_thenReportInformationAreAddedChronologically() {
    // given
    ChefReport chefReport = new ChefReport();
    String firstDate = "2150-07-22";
    String secondDate = "2150-07-24";
    Set<Chef> chefs = SOME_CHEFS;
    BigDecimal totalPrice = A_TOTAL_PRICE;

    // when
    chefReport.addChefReportInformation(secondDate, chefs, totalPrice);
    chefReport.addChefReportInformation(firstDate, chefs, totalPrice);

    // then
    assertThat(chefReport.getChefReportInformation().get(0).getDate()).isEqualTo(firstDate);
    assertThat(chefReport.getChefReportInformation().get(1).getDate()).isEqualTo(secondDate);
  }

}