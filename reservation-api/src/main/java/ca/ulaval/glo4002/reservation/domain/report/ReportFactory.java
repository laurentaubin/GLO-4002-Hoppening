package ca.ulaval.glo4002.reservation.domain.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;

public class ReportFactory {

  private final DailyIngredientReportInformationFactory dailyIngredientReportInformationFactory;

  public ReportFactory(DailyIngredientReportInformationFactory dailyIngredientReportInformationFactory) {
    this.dailyIngredientReportInformationFactory = dailyIngredientReportInformationFactory;
  }

  public Report create(IngredientPriceCalculator ingredientPriceCalculator,
                       Map<LocalDate, Map<IngredientName, BigDecimal>> dailyIngredientsQuantities)
  {
    Map<LocalDate, DailyIngredientReportInformation> dailyIngredientsReportInformation = new HashMap<>();
    for (LocalDate date : dailyIngredientsQuantities.keySet()) {
      dailyIngredientsReportInformation.put(date,
                                            dailyIngredientReportInformationFactory.create(ingredientPriceCalculator,
                                                                                           dailyIngredientsQuantities.get(date)));
    }
    return new Report(dailyIngredientsReportInformation);
  }
}
