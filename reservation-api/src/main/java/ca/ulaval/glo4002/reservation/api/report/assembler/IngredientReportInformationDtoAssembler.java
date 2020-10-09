package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;

public class IngredientReportInformationDtoAssembler {
  public List<IngredientsReportInformationDto> assembleFromIngredientReportInformations(Set<IngredientReportInformation> ingredientReportInformations) {
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = new ArrayList<>();
    for (IngredientReportInformation ingredientReportInformation : ingredientReportInformations) {
      ingredientsReportInformationDtos.add(new IngredientsReportInformationDto(ingredientReportInformation.getIngredientName()
                                                                                                          .toString(),
                                                                               BigDecimal.valueOf(ingredientReportInformation.getQuantity())
                                                                                         .setScale(2,
                                                                                                   RoundingMode.HALF_UP),
                                                                               ingredientReportInformation.getTotalPrice()));
    }
    ingredientsReportInformationDtos.sort(Comparator.comparing(IngredientsReportInformationDto::getIngredientName));
    return ingredientsReportInformationDtos;
  }
}
