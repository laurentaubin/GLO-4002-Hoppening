package ca.ulaval.glo4002.reservation.domain.report.chef;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefPriority;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChefReportTest {

  private static final ChefPriority A_CHEF_TYPE = ChefPriority.FIRST;
  private static final String A_CHEF_NAME = "Thierry Aki";
  private static final Set<RestrictionType> SOME_SPECIALTIES = Set.of(RestrictionType.NONE);
  private static final Chef A_CHEF = new Chef(A_CHEF_NAME, A_CHEF_TYPE, SOME_SPECIALTIES);
  private static final Set<Chef> SOME_CHEFS = Set.of(A_CHEF);
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;

  private ChefReport chefReport;

  @BeforeEach
  public void setUp() {
    chefReport = new ChefReport();
  }

  @Test
  public void whenAddingReportInformation_thenReportInformationAreAddedChronologically() {
    // given
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

  @Test
  public void givenAChefReportInformation_whenCalculateTotalCost_thenSumAllIndividualCosts() {
    // given
    chefReport.addChefReportInformation("2150-07-22", SOME_CHEFS, A_TOTAL_PRICE);

    // when
    BigDecimal totalPrice = chefReport.calculateTotalCost();

    // then
    assertThat(totalPrice).isEqualTo(A_TOTAL_PRICE);
  }

}
