package ca.ulaval.glo4002.reservation.domain.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.exception.IngredientNotFoundException;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class IngredientPriceCalculator {
  private HashMap<IngredientName, BigDecimal> archive;

  public IngredientPriceCalculator() {
    this.archive = new HashMap<>();
  }

  public void generatePriceMapper(List<IngredientPriceDto> ingredientPriceDtos) {
    for (IngredientPriceDto ingredientPriceDto : ingredientPriceDtos) {
      if (IngredientName.contains(ingredientPriceDto.getName())) {
        archive.put(IngredientName.valueOfName(ingredientPriceDto.getName()),
                    ingredientPriceDto.getPricePerKg());
      }
    }
  }

  public BigDecimal getTotalPrice(IngredientName ingredientName, BigDecimal quantity) {
    try {
      return archive.get(ingredientName).multiply(quantity);
    } catch (NullPointerException e) {
      throw new IngredientNotFoundException();
    }
  }
}
