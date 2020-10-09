package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.api.report.dto.TotalReportDto;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;

public class TotalReportDtoAssembler {
  private final IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler;

  public TotalReportDtoAssembler(IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler) {
    this.ingredientReportInformationDtoAssembler = ingredientReportInformationDtoAssembler;
  }

  public TotalReportDto assemble(TotalReport totalReport) {
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(totalReport.getIngredientsReportInformation());
    return new TotalReportDto(ingredientsReportInformationDtos, totalReport.getTotalPrice());
  }
}
