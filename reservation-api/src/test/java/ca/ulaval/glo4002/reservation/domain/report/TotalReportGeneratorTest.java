package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReportGenerator;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

@ExtendWith(MockitoExtension.class)
class TotalReportGeneratorTest {
  private static final BigDecimal PEPPERONI_TOTAL_QUANTITY = BigDecimal.valueOf(30);
  private static final BigDecimal KIWI_TOTAL_QUANTITY = BigDecimal.valueOf(24);
  private static final BigDecimal MARMALADE_TOTAL_QUANTITY = BigDecimal.valueOf(45);
  private static final BigDecimal PEPPERONI_TOTAL_PRICE = BigDecimal.valueOf(100);
  private static final BigDecimal KIWI_TOTAL_PRICE = BigDecimal.valueOf(80);
  private static final BigDecimal MARMALADE_TOTAL_PRICE = BigDecimal.valueOf(75);
  private static final BigDecimal FINAL_TOTAL_PRICE = BigDecimal.valueOf(255);
  private static final LocalDate A_DATE = LocalDate.of(2050, 7, 20);
  private static final LocalDate ANOTHER_DATE = LocalDate.of(2050, 7, 23);

  @Mock
  private IngredientPriceCalculator ingredientPriceCalculator;

  private TotalReportGenerator totalReportGenerator;

  @BeforeEach
  public void setUp() {
    totalReportGenerator = new TotalReportGenerator(ingredientPriceCalculator);
  }

  @Test
  public void givenIngredientPricesAndQuantities_whenGenerateReport_thenReportIsGenerated() {
    // given
    List<IngredientPriceDto> ingredientPrices = new ArrayList<>();
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = givenTotalIngredientsQuantityPerDay();

    // when
    TotalReport actualTotalReport = totalReportGenerator.generateReport(ingredientPrices,
                                                                        ingredientsQuantity);

    // then
    assertThat(actualTotalReport.getTotalPrice()).isEqualTo(FINAL_TOTAL_PRICE);
    assertThat(actualTotalReport.getIngredientsReportInformation()).isEqualTo(givenExpectedReportInfo());
  }

  private Map<LocalDate, Map<IngredientName, BigDecimal>> givenTotalIngredientsQuantityPerDay() {
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(A_DATE, givenIngredientsQuantityForADate());
    ingredientsQuantity.put(ANOTHER_DATE, givenIngredientsQuantityForAnotherDate());
    return ingredientsQuantity;
  }

  private Set<IngredientReportInformation> givenExpectedReportInfo() {
    Set<IngredientReportInformation> set = new HashSet<>();
    set.add(new IngredientReportInformation(IngredientName.PEPPERONI,
                                            PEPPERONI_TOTAL_QUANTITY,
                                            PEPPERONI_TOTAL_PRICE));
    set.add(new IngredientReportInformation(IngredientName.KIWI,
                                            KIWI_TOTAL_QUANTITY,
                                            KIWI_TOTAL_PRICE));
    set.add(new IngredientReportInformation(IngredientName.MARMALADE,
                                            MARMALADE_TOTAL_QUANTITY,
                                            MARMALADE_TOTAL_PRICE));
    return set;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantityForADate() {
    Map<IngredientName, BigDecimal> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(IngredientName.PEPPERONI, PEPPERONI_TOTAL_QUANTITY);
    given(ingredientPriceCalculator.getTotalPrice(IngredientName.PEPPERONI,
                                                  PEPPERONI_TOTAL_QUANTITY)).willReturn(PEPPERONI_TOTAL_PRICE);
    ingredientsQuantity.put(IngredientName.KIWI, KIWI_TOTAL_QUANTITY);
    given(ingredientPriceCalculator.getTotalPrice(IngredientName.KIWI,
                                                  KIWI_TOTAL_QUANTITY)).willReturn(KIWI_TOTAL_PRICE);
    return ingredientsQuantity;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantityForAnotherDate() {
    Map<IngredientName, BigDecimal> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(IngredientName.MARMALADE, MARMALADE_TOTAL_QUANTITY);
    given(ingredientPriceCalculator.getTotalPrice(IngredientName.MARMALADE,
                                                  MARMALADE_TOTAL_QUANTITY)).willReturn(MARMALADE_TOTAL_PRICE);
    return ingredientsQuantity;
  }
}
