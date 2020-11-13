package ca.ulaval.glo4002.reservation.domain.report.chef;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefType;

@ExtendWith(MockitoExtension.class)
public class ChefReportGeneratorTest {

  private static final ChefType A_CHEF_TYPE = ChefType.BOB_SMARTIES;
  private static final ChefType ANOTHER_CHEF_TYPE = ChefType.AMELIE_MELO;
  private static final LocalDate A_DATE = LocalDate.of(Integer.parseInt("2020"),
                                                       Integer.parseInt("12"),
                                                       Integer.parseInt("10"));
  private static final BigDecimal EXPECTED_PRICE_FOR_TWO_CHEF = BigDecimal.valueOf(12000);
  private static final BigDecimal EXPECTED_PRICE_FOR_NO_CHEF = BigDecimal.ZERO;

  private ChefReportGenerator chefReportGenerator;
  private Chef aChef;
  private Chef anotherChef;

  @BeforeEach
  public void setUpChefGenerator() {
    chefReportGenerator = new ChefReportGenerator();
    aChef = new Chef(A_CHEF_TYPE);
    anotherChef = new Chef(ANOTHER_CHEF_TYPE);
  }

  @Test
  public void givenTwoChefs_whenGenerateReport_thenTheReportShouldHavePriceOfTwoChef() {
    // given
    Map<LocalDate, Set<Chef>> chefsByDay = givenADayWithTwoChef();

    // when
    ChefReport chefReport = chefReportGenerator.generateReport(chefsByDay);

    BigDecimal actualPrice = chefReport.getChefReportInformation().get(0).getTotalPrice();
    assertThat(actualPrice).isEqualTo(EXPECTED_PRICE_FOR_TWO_CHEF);
  }

  @Test
  public void givenNoChef_whenGenerateReport_thenReportShouldHavePriceZero() {
    // given
    Map<LocalDate, Set<Chef>> chefsByDay = givenADayOfNoChef();

    // when
    ChefReport chefReport = chefReportGenerator.generateReport(chefsByDay);

    // then
    BigDecimal actualPrice = chefReport.getChefReportInformation().get(0).getTotalPrice();
    assertThat(actualPrice).isEqualTo(EXPECTED_PRICE_FOR_NO_CHEF);
  }

  @Test
  public void givenTwoChefs_whenGenerateReport_thenReportShouldHaveTwoChefs() {
    // given
    Map<LocalDate, Set<Chef>> chefsByDay = givenADayWithTwoChef();
    Set<Chef> expectedChefs = Set.of(aChef, anotherChef);

    // when
    ChefReport chefReport = chefReportGenerator.generateReport(chefsByDay);

    // then
    assertThat(chefReport.getChefReportInformation().get(0).getChefs()).isEqualTo(expectedChefs);
  }

  private Map<LocalDate, Set<Chef>> givenADayWithTwoChef() {
    Map<LocalDate, Set<Chef>> restaurantChefs = new HashMap<>();
    Set<Chef> chefs = Set.of(aChef, anotherChef);
    restaurantChefs.put(A_DATE, chefs);
    return restaurantChefs;
  }

  private Map<LocalDate, Set<Chef>> givenADayOfNoChef() {
    Map<LocalDate, Set<Chef>> restaurantChefs = new HashMap<>();
    restaurantChefs.put(A_DATE, Collections.emptySet());
    return restaurantChefs;
  }
}