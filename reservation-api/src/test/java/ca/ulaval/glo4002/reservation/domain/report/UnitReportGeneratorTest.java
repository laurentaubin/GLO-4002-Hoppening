package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportDay;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportGenerator;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class UnitReportGeneratorTest {
  private static final String PEPPERONI_NAME = IngredientName.PEPPERONI.toString();
  private static final BigDecimal PEPPERONI_TOTAL_QUANTITY = BigDecimal.valueOf(30);
  private static final BigDecimal PEPPERONI_PRICE = BigDecimal.valueOf(6.99);
  private static final BigDecimal PEPPERONI_TOTAL_PRICE = BigDecimal.valueOf(209.7);

  private static final String KIWI_NAME = IngredientName.KIWI.toString();
  private static final BigDecimal KIWI_TOTAL_QUANTITY = BigDecimal.valueOf(24);
  private static final BigDecimal KIWI_PRICE = BigDecimal.valueOf(0.99);
  private static final BigDecimal KIWI_TOTAL_PRICE = BigDecimal.valueOf(23.76);

  private static final String MARMALADE_NAME = IngredientName.MARMALADE.toString();
  private static final BigDecimal MARMALADE_TOTAL_QUANTITY = BigDecimal.valueOf(45);
  private static final BigDecimal MARMALADE_PRICE = BigDecimal.valueOf(5.29);
  private static final BigDecimal MARMALADE_TOTAL_PRICE = BigDecimal.valueOf(238.05);

  private static final LocalDate A_DATE = LocalDate.of(2050, 7, 20);
  private static final LocalDate ANOTHER_DATE = LocalDate.of(2050, 7, 23);

  private static final BigDecimal TOTAL_UNIT_PRICE_FOR_A_DATE = BigDecimal.valueOf(233.46);
  private static final BigDecimal TOTAL_UNIT_PRICE_FOR_ANOTHER_DATE = BigDecimal.valueOf(238.05);
  private static final BigDecimal TOTAL_PRICE_FOR_ENTIRE_PERIOD = BigDecimal.valueOf(471.510);

  private UnitReportGenerator unitReportGenerator;

  @BeforeEach
  public void setUp() {
    unitReportGenerator = new UnitReportGenerator();
  }

  @Test
  public void givenIngredientsPricesAndQuantity_whenGenerateReport_thenProducesValidReport() {
    // given
    List<IngredientPriceDto> ingredientPriceDtos = givenIngredientsPrices();
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = givenTotalIngredientsQuantityPerDay();
    UnitReport expectedUnitReport = givenExpectedUnitReport();

    // when
    UnitReport unitReport = unitReportGenerator.generateReport(ingredientPriceDtos,
                                                               ingredientsQuantity);

    // then
    assertThat(unitReport.getTotalPrice()
                         .doubleValue()).isEqualTo(expectedUnitReport.getTotalPrice()
                                                                     .doubleValue());
    assertThat(unitReport.getUnitReportDays()
                         .size()).isEqualTo(expectedUnitReport.getUnitReportDays().size());
  }

  private List<IngredientPriceDto> givenIngredientsPrices() {
    List<IngredientPriceDto> ingredientPriceDtos = new ArrayList<>();
    ingredientPriceDtos.add(new IngredientPriceDto(PEPPERONI_NAME, PEPPERONI_PRICE));
    ingredientPriceDtos.add(new IngredientPriceDto(KIWI_NAME, KIWI_PRICE));
    ingredientPriceDtos.add(new IngredientPriceDto(MARMALADE_NAME, MARMALADE_PRICE));
    return ingredientPriceDtos;
  }

  private Map<LocalDate, Map<IngredientName, BigDecimal>> givenTotalIngredientsQuantityPerDay() {
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(A_DATE, givenIngredientsQuantityForADate());
    ingredientsQuantity.put(ANOTHER_DATE, givenIngredientsQuantityForAnotherDate());
    return ingredientsQuantity;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantityForADate() {
    Map<IngredientName, BigDecimal> ingredientsQuantity = new HashMap<>();
    ingredientsQuantity.put(IngredientName.PEPPERONI, PEPPERONI_TOTAL_QUANTITY);
    ingredientsQuantity.put(IngredientName.KIWI, KIWI_TOTAL_QUANTITY);
    return ingredientsQuantity;
  }

  private Map<IngredientName, BigDecimal> givenIngredientsQuantityForAnotherDate() {
    Map<IngredientName, BigDecimal> ingredientsQuantity = new HashMap<>();
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
