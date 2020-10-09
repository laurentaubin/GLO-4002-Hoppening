package ca.ulaval.glo4002.reservation.domain.report.unit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class UnitReportGenerator {
  public UnitReport generateReport(List<IngredientPriceDto> ingredientPrices,
                                   Map<LocalDate, Map<IngredientName, Double>> ingredientsQuantity)
  {
    List<UnitReportDay> unitReportDays = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (Map.Entry<LocalDate, Map<IngredientName, Double>> entry : ingredientsQuantity.entrySet()) {
      LocalDate date = entry.getKey();
      Map<IngredientName, Double> ingredientsQuantityForDate = entry.getValue();
      UnitReportDay unitReportDay = generateUnitReportDay(ingredientPrices,
                                                          ingredientsQuantityForDate,
                                                          date);
      unitReportDays.add(unitReportDay);
      totalPrice = totalPrice.add(unitReportDay.getTotalPrice()).setScale(2, RoundingMode.CEILING);
    }
    return new UnitReport(unitReportDays, totalPrice);
  }

  private UnitReportDay generateUnitReportDay(List<IngredientPriceDto> ingredientPrices,
                                              Map<IngredientName, Double> ingredientsQuantity,
                                              LocalDate date)
  {
    Set<IngredientReportInformation> ingredientReportInformations = new HashSet<>();
    BigDecimal totalPriceAtDate = BigDecimal.ZERO;

    for (Map.Entry<IngredientName, Double> ingredientQuantity : ingredientsQuantity.entrySet()) {
      IngredientName ingredientName = ingredientQuantity.getKey();
      double quantity = ingredientQuantity.getValue();

      BigDecimal totalIngredientPrice = calculateTotalIngredientPrice(ingredientPrices,
                                                                      ingredientName,
                                                                      quantity);
      ingredientReportInformations.add(new IngredientReportInformation(ingredientName,
                                                                       quantity,
                                                                       totalIngredientPrice.setScale(2,
                                                                                                     RoundingMode.CEILING)));
      totalPriceAtDate = totalPriceAtDate.add(totalIngredientPrice)
                                         .setScale(2, RoundingMode.CEILING);
    }
    return new UnitReportDay(date, ingredientReportInformations, totalPriceAtDate);
  }

  private BigDecimal calculateTotalIngredientPrice(List<IngredientPriceDto> ingredientPrices,
                                                   IngredientName ingredientName,
                                                   double quantity)
  {
    BigDecimal totalIngredientPrice = BigDecimal.valueOf(0);
    for (IngredientPriceDto ingredientPriceDto : ingredientPrices) {
      if (ingredientPriceDto.getName().equals(ingredientName.toString())) {
        totalIngredientPrice = ingredientPriceDto.getPricePerKg()
                                                 .multiply(BigDecimal.valueOf(quantity))
                                                 .setScale(2, RoundingMode.CEILING);
        return totalIngredientPrice;
      }
    }
    return totalIngredientPrice;
  }
}
