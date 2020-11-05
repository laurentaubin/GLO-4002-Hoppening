package ca.ulaval.glo4002.reservation.api.report.presenter.unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDayDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDto;
import ca.ulaval.glo4002.reservation.domain.report.DailyIngredientReportInformation;
import ca.ulaval.glo4002.reservation.domain.report.Report;

public class UnitReportDtoFactory {
  private UnitReportDayDtoFactory unitReportDayDtoFactory;

  public UnitReportDtoFactory(UnitReportDayDtoFactory unitReportDayDtoFactory) {
    this.unitReportDayDtoFactory = unitReportDayDtoFactory;
  }

  public UnitReportDto create(Report report) {
    List<UnitReportDayDto> ingredientsInfo = generateDailyIngredientReportInformationDto(report);
    return new UnitReportDto(ingredientsInfo);
  }

  private List<UnitReportDayDto> generateDailyIngredientReportInformationDto(Report report) {
    List<UnitReportDayDto> dailyIngredientReportInformationDtos = new ArrayList<>();
    for (Map.Entry<LocalDate, DailyIngredientReportInformation> dailyIngredientInformationEntry : report.getDailyIngredientsInformation()
                                                                                                        .entrySet())
    {
      dailyIngredientReportInformationDtos.add(unitReportDayDtoFactory.create(dailyIngredientInformationEntry.getKey(),
                                                                              dailyIngredientInformationEntry.getValue()));
    }
    dailyIngredientReportInformationDtos.sort(Comparator.comparing(UnitReportDayDto::getDate));
    return dailyIngredientReportInformationDtos;
  }
}
