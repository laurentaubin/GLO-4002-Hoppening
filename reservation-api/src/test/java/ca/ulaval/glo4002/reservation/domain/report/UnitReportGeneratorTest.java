package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class UnitReportGeneratorTest {
  private static final String PEPPERONI_NAME = IngredientName.PEPPERONI.toString();
  private static final double PEPPERONI_TOTAL_QUANTITY = 30;
  private static final BigDecimal PEPPERONI_PRICE = BigDecimal.valueOf(6.99);
  private static final BigDecimal PEPPERONI_TOTAL_PRICE = BigDecimal.valueOf(209.70).setScale(2);

  private static final String KIWI_NAME = IngredientName.KIWI.toString();
  private static final double KIWI_TOTAL_QUANTITY = 24;
  private static final BigDecimal KIWI_PRICE = BigDecimal.valueOf(0.99);
  private static final BigDecimal KIWI_TOTAL_PRICE = BigDecimal.valueOf(23.76);

  private static final String MARMALADE_NAME = IngredientName.MARMALADE.toString();
  private static final double MARMALADE_TOTAL_QUANTITY = 45;
  private static final BigDecimal MARMALADE_PRICE = BigDecimal.valueOf(5.29);
  private static final BigDecimal MARMALADE_TOTAL_PRICE = BigDecimal.valueOf(238.05);

  private static final LocalDate A_DATE = LocalDate.of(2050, 7, 20);
  private static final LocalDate ANOTHER_DATE = LocalDate.of(2050, 7, 23);

  private static final BigDecimal TOTAL_UNIT_PRICE_FOR_A_DATE = BigDecimal.valueOf(233.46);
  private static final BigDecimal TOTAL_UNIT_PRICE_FOR_ANOTHER_DATE = BigDecimal.valueOf(238.05);
  private static final BigDecimal TOTAL_PRICE_FOR_ENTIRE_PERIOD = BigDecimal.valueOf(471.51);

  private UnitReportGenerator unitReportGenerator;

  @BeforeEach
  public void setUp() {
    unitReportGenerator = new UnitReportGenerator();
  }

  @Test
  public void givenIngredientsPricesAndQuantity_whenGenerateReport_thenProducesValidReport() {
    // given
    List<IngredientPriceDto> ingredientPriceDtos = givenIngredientsPrices();
    Map<LocalDate, Map<IngredientName, Double>> ingredientsQuantity = givenTotalIngredientsQuantityPerDay();
    UnitReport expectedUnitReport = givenExpectedUnitReport();

    // when
    UnitReport unitReport = unitReportGenerator.generateReport(ingredientPriceDtos,
                                                               ingredientsQuantity);

    // then
    assertThat(unitReport).isEqualTo(expectedUnitReport);
  }

  private List<IngredientPriceDto> givenIngredientsPrices() {
    List<IngredientPriceDto> ingredientPriceDtos = new ArrayList<>();
    ingredientPriceDtos.add(new IngredientPriceDto(PEPPERONI_NAME, PEPPERONI_PRICE));
    ingredientPriceDtos.add(new IngredientPriceDto(KIWI_NAME, KIWI_PRICE));
    ingredientPriceDtos.add(new IngredientPriceDto(MARMALADE_NAME, MARMALADE_PRICE));
    return ingredientPriceDtos;
  }

  private Map<LocalDate, Map<IngredientName, Double>> givenTotalIngredientsQuantityPerDay() {
    Map<LocalDate, Map<IngredientName, Double>> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(A_DATE, givenIngredientsQuantityForADate());
    ingredientsQuantity.put(ANOTHER_DATE, givenIngredientsQuantityForAnotherDate());
    return ingredientsQuantity;
  }

  private Map<IngredientName, Double> givenIngredientsQuantityForADate() {
    Map<IngredientName, Double> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(IngredientName.PEPPERONI, PEPPERONI_TOTAL_QUANTITY);
    ingredientsQuantity.put(IngredientName.KIWI, KIWI_TOTAL_QUANTITY);
    return ingredientsQuantity;
  }

  private Map<IngredientName, Double> givenIngredientsQuantityForAnotherDate() {
    Map<IngredientName, Double> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(IngredientName.MARMALADE, MARMALADE_TOTAL_QUANTITY);
    return ingredientsQuantity;
  }

  private UnitReport givenExpectedUnitReport() {
    List<UnitReportDay> unitReportDays = Arrays.asList(givenExpectedUnitReportLineForADate(),
                                                       givenExpectedUnitReportLineForAnotherDate());
    return new UnitReport(unitReportDays, TOTAL_PRICE_FOR_ENTIRE_PERIOD);

  }

  private UnitReportDay givenExpectedUnitReportLineForADate() {
    IngredientReportInformation pepperoniReportInformation = new IngredientReportInformation(IngredientName.PEPPERONI,
                                                                                             PEPPERONI_TOTAL_QUANTITY,
                                                                                             PEPPERONI_TOTAL_PRICE);
    IngredientReportInformation kiwiReportInformation = new IngredientReportInformation(IngredientName.KIWI,
                                                                                        KIWI_TOTAL_QUANTITY,
                                                                                        KIWI_TOTAL_PRICE);
    return new UnitReportDay(A_DATE,
                             Set.of(pepperoniReportInformation, kiwiReportInformation),
                             TOTAL_UNIT_PRICE_FOR_A_DATE);
  }

  private UnitReportDay givenExpectedUnitReportLineForAnotherDate() {
    IngredientReportInformation marmaladeReportInformation = new IngredientReportInformation(IngredientName.MARMALADE,
                                                                                             MARMALADE_TOTAL_QUANTITY,
                                                                                             MARMALADE_TOTAL_PRICE);
    return new UnitReportDay(ANOTHER_DATE,
                             Set.of(marmaladeReportInformation),
                             TOTAL_UNIT_PRICE_FOR_ANOTHER_DATE);
  }
}
