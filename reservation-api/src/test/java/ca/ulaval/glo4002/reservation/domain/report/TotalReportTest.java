package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;

class TotalReportTest {
  private TotalReport totalReport;
  private static final BigDecimal AN_OTHER_INGREDIENT_TOTAL_QUANTITY = BigDecimal.valueOf(30);
  private static final BigDecimal AN_OTHER_INGREDIENT_TOTAL_PRICE = BigDecimal.valueOf(209.70)
                                                                              .setScale(2);
  private final IngredientName AN_OTHER_INGREDIENT_NAME = IngredientName.PEPPERONI;
  private static final BigDecimal AN_INGREDIENT_TOTAL_QUANTITY = BigDecimal.valueOf(24);
  private static final BigDecimal AN_INGREDIENT_TOTAL_PRICE = BigDecimal.valueOf(23.76);
  private final IngredientName AN_INGREDIENT_NAME = IngredientName.KIWI;

  @BeforeEach
  public void setUp() {
    totalReport = new TotalReport();
  }

  @Test
  public void givenEmptyTotalReport_whenGetIngredientsReportInfo_thenIngredientsReportInfoIsEmpty() {
    // when
    Set ingredientsReportInfo = totalReport.getIngredientsReportInformation();

    // then
    assertThat(ingredientsReportInfo).isEmpty();
  }

  @Test
  public void givenUpdatedTotalReport_whenAdd_thenShouldAddToIngredientsReportInformation() {
    // given
    IngredientReportInformation anOtherIngredient = givenAnOtherIngredient();

    // when
    totalReport.add(anOtherIngredient);

    // then
    assertThat(totalReport.getIngredientsReportInformation().contains(anOtherIngredient)).isTrue();
  }

  @Test
  public void givenCreatedTotalReport_whenAdd_thenShouldUpdateTotalPrice() {
    // given
    IngredientReportInformation anIngredient = givenAnIngredient();

    // when
    totalReport.add(anIngredient);

    // then
    assertThat(totalReport.getTotalPrice()).isEqualTo(AN_INGREDIENT_TOTAL_PRICE);
  }

  @Test
  public void givenAddedSameIngredientTwice_whenAdd_thenShouldUpdateIngredientsReportAndTotalPrice() {
    // given
    IngredientReportInformation anIngredient = givenAnIngredient();
    totalReport.add(anIngredient);
    IngredientReportInformation aSecondIngredient = givenAnIngredient();
    IngredientReportInformation expectedKiwi = new IngredientReportInformation(AN_INGREDIENT_NAME,
                                                                               AN_INGREDIENT_TOTAL_QUANTITY.multiply(BigDecimal.valueOf(2)),
                                                                               AN_INGREDIENT_TOTAL_PRICE.multiply(BigDecimal.valueOf(2)));

    // when
    totalReport.add(aSecondIngredient);

    // then
    assertThat(totalReport.getTotalPrice()).isEqualTo(AN_INGREDIENT_TOTAL_PRICE.multiply(BigDecimal.valueOf(2)));
    assertThat(totalReport.getIngredientsReportInformation().size()).isEqualTo(1);
    IngredientReportInformation actualIngredient = (IngredientReportInformation) totalReport.getIngredientsReportInformation()
                                                                                            .toArray()[0];
    assertThat(actualIngredient.getQuantity()).isEqualTo(expectedKiwi.getQuantity());
    assertThat(actualIngredient.getTotalPrice()).isEqualTo(expectedKiwi.getTotalPrice());
  }

  private IngredientReportInformation givenAnOtherIngredient() {
    return new IngredientReportInformation(AN_OTHER_INGREDIENT_NAME,
                                           AN_OTHER_INGREDIENT_TOTAL_QUANTITY,
                                           AN_OTHER_INGREDIENT_TOTAL_PRICE);
  }

  private IngredientReportInformation givenAnIngredient() {
    return new IngredientReportInformation(AN_INGREDIENT_NAME,
                                           AN_INGREDIENT_TOTAL_QUANTITY,
                                           AN_INGREDIENT_TOTAL_PRICE);
  }
}
