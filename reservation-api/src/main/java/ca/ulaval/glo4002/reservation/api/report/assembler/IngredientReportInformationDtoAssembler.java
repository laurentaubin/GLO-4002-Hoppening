package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.domain.report.UnitReportDay;

public class IngredientReportInformationDtoAssembler {
  public List<IngredientsReportInformationDto> assemble(UnitReportDay unitReportDay) {
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = new ArrayList<>();
    for (IngredientReportInformation ingredientReportInformation : unitReportDay.getIngredientsReportInformation()) {
      ingredientsReportInformationDtos.add(new IngredientsReportInformationDto(ingredientReportInformation.getIngredient()
                                                                                                          .toString(),
                                                                               BigDecimal.valueOf(ingredientReportInformation.getQuantity())
                                                                                         .setScale(2),
                                                                               ingredientReportInformation.getTotalPrice()));
    }
    ingredientsReportInformationDtos.sort(Comparator.comparing(IngredientsReportInformationDto::getIngredientName));
    return ingredientsReportInformationDtos;
  }
}
