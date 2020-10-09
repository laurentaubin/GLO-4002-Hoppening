package ca.ulaval.glo4002.reservation.domain.report.unit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class UnitReportGenerator {
  public UnitReport generateReport(List<IngredientPriceDto> ingredientPrices,
                                   Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity)
  {
    List<UnitReportDay> unitReportDays = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (Map.Entry<LocalDate, Map<IngredientName, BigDecimal>> entry : ingredientsQuantity.entrySet()) {
      LocalDate date = entry.getKey();
      Map<IngredientName, BigDecimal> ingredientsQuantityForDate = entry.getValue();
      UnitReportDay unitReportDay = generateUnitReportDay(ingredientPrices,
                                                          ingredientsQuantityForDate,
                                                          date);
      unitReportDays.add(unitReportDay);
      totalPrice = totalPrice.add(unitReportDay.getTotalPrice());
    }
    return new UnitReport(unitReportDays, totalPrice);
  }

  private UnitReportDay generateUnitReportDay(List<IngredientPriceDto> ingredientPrices,
                                              Map<IngredientName, BigDecimal> ingredientsQuantity,
                                              LocalDate date)
  {
    Set<IngredientReportInformation> ingredientReportInformations = new HashSet<>();
    BigDecimal totalPriceAtDate = BigDecimal.ZERO;

    for (Map.Entry<IngredientName, BigDecimal> ingredientQuantity : ingredientsQuantity.entrySet()) {
      IngredientName ingredientName = ingredientQuantity.getKey();
      BigDecimal quantity = ingredientQuantity.getValue();

      BigDecimal totalIngredientPrice = calculateTotalIngredientPrice(ingredientPrices,
                                                                      ingredientName,
                                                                      quantity);
      ingredientReportInformations.add(new IngredientReportInformation(ingredientName,
                                                                       quantity,
                                                                       totalIngredientPrice));
      totalPriceAtDate = totalPriceAtDate.add(totalIngredientPrice);
    }
    return new UnitReportDay(date, ingredientReportInformations, totalPriceAtDate);
  }

  private BigDecimal calculateTotalIngredientPrice(List<IngredientPriceDto> ingredientPrices,
                                                   IngredientName ingredientName,
                                                   BigDecimal quantity)
  {
    BigDecimal totalIngredientPrice = BigDecimal.valueOf(0);
    for (IngredientPriceDto ingredientPriceDto : ingredientPrices) {
      if (ingredientPriceDto.getName().equals(ingredientName.toString())) {
        totalIngredientPrice = ingredientPriceDto.getPricePerKg().multiply(quantity);
        return totalIngredientPrice;
      }
    }
    return totalIngredientPrice;
  }
}
