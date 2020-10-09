package ca.ulaval.glo4002.reservation.domain.report.total;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.IngredientPriceCalculator;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class TotalReportGenerator {
  private IngredientPriceCalculator ingredientPriceCalculator;

  public TotalReportGenerator(IngredientPriceCalculator ingredientPriceCalculator) {
    this.ingredientPriceCalculator = ingredientPriceCalculator;
  }

  public TotalReport generateReport(List<IngredientPriceDto> ingredientPrices,
                                    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity)
  {
    ingredientPriceCalculator.generatePriceMapper(ingredientPrices);
    Map<IngredientName, BigDecimal> compressedMap = extractIngredientMapFromDateMap(ingredientsQuantity);
    TotalReport totalReport = new TotalReport();

    compressedMap.forEach((ingredientName, quantity) -> {
      IngredientReportInformation ingredientReportInformation = new IngredientReportInformation(ingredientName,
                                                                                                quantity,
                                                                                                ingredientPriceCalculator.getTotalPrice(ingredientName,
                                                                                                                                        quantity));
      totalReport.add(ingredientReportInformation);
    });

    return totalReport;
  }

  private Map<IngredientName, BigDecimal> extractIngredientMapFromDateMap(Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity) {
    Map<IngredientName, BigDecimal> compressedMap = new HashMap<>();
    for (Map<IngredientName, BigDecimal> iteratorMap : ingredientsQuantity.values()) {
      for (IngredientName ingredientName : iteratorMap.keySet()) {
        if (compressedMap.containsKey(ingredientName)) {
          compressedMap.put(ingredientName,
                            compressedMap.get(ingredientName).add(iteratorMap.get(ingredientName)));
        } else {
          compressedMap.put(ingredientName, iteratorMap.get(ingredientName));
        }
      }
    }
    return compressedMap;
  }
}
