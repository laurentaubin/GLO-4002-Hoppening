package ca.ulaval.glo4002.reservation.api.report.assembler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDayDto;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportDay;

public class UnitReportDayDtoAssembler {

  private IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler;

  public UnitReportDayDtoAssembler(IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler) {
    this.ingredientReportInformationDtoAssembler = ingredientReportInformationDtoAssembler;
  }

  public List<UnitReportDayDto> assembleUnitReportDayDtos(List<UnitReportDay> unitReportDays) {
    List<UnitReportDayDto> unitReportDayDtos = new ArrayList<>();
    unitReportDays.sort(Comparator.comparing(UnitReportDay::getDate));
    for (UnitReportDay unitReportDay : unitReportDays) {
      List<IngredientsReportInformationDto> ingredientsReportInformationDtos = ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(unitReportDay.getIngredientsReportInformation());
      unitReportDayDtos.add(new UnitReportDayDto(unitReportDay.getDate(),
                                                 ingredientsReportInformationDtos,
                                                 unitReportDay.getTotalPrice()));
    }
    return unitReportDayDtos;
  }
}
