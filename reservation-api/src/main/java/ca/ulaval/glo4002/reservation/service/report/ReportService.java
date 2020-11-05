package ca.ulaval.glo4002.reservation.service.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
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

  public ReportService(IngredientQuantityRepository ingredientQuantityRepository,
                       IngredientPriceRepository ingredientPriceRepository,
                       ReportGenerator reportGenerator)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.reportGenerator = reportGenerator;
  }

  public Report getReportResponse(ReportPeriod reportPeriod) {
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, BigDecimal>> dateToIngredientQuantities = ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return reportGenerator.generateReport(ingredientPrices, dateToIngredientQuantities);
  }
}
