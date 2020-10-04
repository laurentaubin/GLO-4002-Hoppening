package ca.ulaval.glo4002.reservation.service.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportType;
import ca.ulaval.glo4002.reservation.domain.report.UnitReport;
import ca.ulaval.glo4002.reservation.domain.report.UnitReportGenerator;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceRepository;

public class ReportService {

  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientPriceRepository ingredientPriceRepository;
  private final UnitReportGenerator unitReportGenerator;

  public ReportService(IngredientQuantityRepository ingredientQuantityRepository,
                       IngredientPriceRepository ingredientPriceRepository,
                       UnitReportGenerator unitReportGenerator)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.unitReportGenerator = unitReportGenerator;
  }

  public UnitReport getUnitReport(ReportPeriod reportPeriod, ReportType reportType) {
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, Double>> ingredientsQuantityPerDay = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return unitReportGenerator.generateReport(ingredientPrices, ingredientsQuantityPerDay);
  }
}
