package ca.ulaval.glo4002.reservation.service.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.Restaurant;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.*;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

public class IngredientReportService {

  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientPriceRepository ingredientPriceRepository;
  private final IngredientReportGenerator ingredientReportGenerator;
  private final Restaurant restaurant;
  private final MaterialReportGenerator materialReportGenerator;
  private final ReportPeriodFactory reportPeriodFactory;

  public IngredientReportService(IngredientQuantityRepository ingredientQuantityRepository,
      IngredientPriceRepository ingredientPriceRepository,
      IngredientReportGenerator ingredientReportGenerator, Restaurant restaurant,
      MaterialReportGenerator materialReportGenerator, ReportPeriodFactory reportPeriodFactory) {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.ingredientReportGenerator = ingredientReportGenerator;
    this.restaurant = restaurant;
    this.materialReportGenerator = materialReportGenerator;
    this.reportPeriodFactory = reportPeriodFactory;
  }

  public IngredientReport getIngredientReport(String startDate, String endDate) {
    ReportPeriod reportPeriod = reportPeriodFactory.create(LocalDate.parse(startDate),
        LocalDate.parse(endDate), restaurant.getHoppeningEvent().getDinnerPeriod());
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, BigDecimal>> dateToIngredientQuantities =
        ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return ingredientReportGenerator.generateReport(ingredientPrices, dateToIngredientQuantities);
  }

  public DinnerPeriodDto getDinnerPeriodDto() {
    return new DinnerPeriodDto(restaurant.getHoppeningEvent().getDinnerPeriod());
  }

  public MaterialReport getMaterialReport(String startDate, String endDate) {
    ReportPeriod reportPeriod = reportPeriodFactory.create(LocalDate.parse(startDate),
        LocalDate.parse(endDate), restaurant.getHoppeningEvent().getDinnerPeriod());
    Map<LocalDate, DailyDishesQuantity> dailyDishesQuantity =
        restaurant.getDailyDishesQuantity(reportPeriod);
    return materialReportGenerator.generateReport(dailyDishesQuantity, reportPeriod);
  }

}
