package ca.ulaval.glo4002.reservation.service.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.material.Buffet;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.IngredientPriceRepository;
import ca.ulaval.glo4002.reservation.domain.report.Report;
import ca.ulaval.glo4002.reservation.domain.report.ReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class ReportService {

  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientPriceRepository ingredientPriceRepository;
  private final ReportGenerator reportGenerator;
  private final MaterialReportGenerator materialReportGenerator;
  private final Buffet buffet;

  public ReportService(IngredientQuantityRepository ingredientQuantityRepository,
                       IngredientPriceRepository ingredientPriceRepository,
                       ReportGenerator reportGenerator,
                       MaterialReportGenerator materialReportGenerator,
                       Buffet buffet)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.reportGenerator = reportGenerator;
    this.materialReportGenerator = materialReportGenerator;
    this.buffet = buffet;
  }

  public Report getIngredientReport(ReportPeriod reportPeriod) {
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, BigDecimal>> dateToIngredientQuantities = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return reportGenerator.generateReport(ingredientPrices, dateToIngredientQuantities);
  }

  public MaterialReport getMaterialReport(ReportPeriod reportPeriod) {
    Map<LocalDate, DailyDishesQuantity> dailyDishesQuantity = buffet.getDailyDishesQuantities(reportPeriod);
    return materialReportGenerator.generateReport(dailyDishesQuantity, reportPeriod);
  }
}
