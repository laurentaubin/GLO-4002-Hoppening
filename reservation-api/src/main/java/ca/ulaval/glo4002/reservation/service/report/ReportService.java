package ca.ulaval.glo4002.reservation.service.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReport;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportGenerator;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceRepository;

public class ReportService {

  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientPriceRepository ingredientPriceRepository;
  private final UnitReportGenerator unitReportGenerator;
  private final TotalReportGenerator totalReportGenerator;

  public ReportService(IngredientQuantityRepository ingredientQuantityRepository,
                       IngredientPriceRepository ingredientPriceRepository,
                       UnitReportGenerator unitReportGenerator,
                       TotalReportGenerator totalReportGenerator)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.unitReportGenerator = unitReportGenerator;
    this.totalReportGenerator = totalReportGenerator;
  }

  public UnitReport getUnitReport(ReportPeriod reportPeriod) {
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantityPerDay = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return unitReportGenerator.generateReport(ingredientPrices, ingredientsQuantityPerDay);
  }

  public TotalReport getTotalReport(ReportPeriod reportPeriod) {
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantityPerDay = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    return totalReportGenerator.generateReport(ingredientPrices, ingredientsQuantityPerDay);
  }
}
