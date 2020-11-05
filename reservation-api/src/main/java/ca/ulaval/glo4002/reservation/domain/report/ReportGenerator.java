package ca.ulaval.glo4002.reservation.domain.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class ReportGenerator {
  private final IngredientPriceCalculatorFactory ingredientPriceCalculatorFactory;
  private final ReportFactory reportFactory;

  public ReportGenerator(IngredientPriceCalculatorFactory ingredientPriceCalculatorFactory,
                         ReportFactory reportFactory)
  {
    this.ingredientPriceCalculatorFactory = ingredientPriceCalculatorFactory;
    this.reportFactory = reportFactory;
  }

  public Report generateReport(List<IngredientPriceDto> ingredientPriceDtos,
                               Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientQuantities)
  {
    IngredientPriceCalculator ingredientPriceCalculator = ingredientPriceCalculatorFactory.create(ingredientPriceDtos);
    return reportFactory.create(ingredientPriceCalculator, ingredientQuantities);
  }
}
