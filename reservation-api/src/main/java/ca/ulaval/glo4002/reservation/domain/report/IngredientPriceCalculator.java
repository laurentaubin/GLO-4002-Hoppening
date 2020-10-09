package ca.ulaval.glo4002.reservation.domain.report;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.exception.IngredientNotFoundException;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

public class IngredientPriceCalculator {
    private HashMap<IngredientName, BigDecimal> archive;


    public IngredientPriceCalculator() {
        this.archive = new HashMap<>();
    }

    public void generatePriceMapper(List<IngredientPriceDto> ingredientPriceDtos) {
        for(IngredientPriceDto ingredientPriceDto : ingredientPriceDtos){
            if (IngredientName.contains(ingredientPriceDto.getName())) {
                archive.put(
                        IngredientName.valueOfName(ingredientPriceDto.getName()),
                        ingredientPriceDto.getPricePerKg());
            }
        }
    }

    public BigDecimal getTotalPrice(IngredientName ingredientName, double quantity) {
        try {
            return archive.get(ingredientName).multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        } catch (NullPointerException e) {
            throw new IngredientNotFoundException();
        }
    }
}
