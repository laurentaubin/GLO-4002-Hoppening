package ca.ulaval.glo4002.reservation.service.report;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReport;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefRepository;
import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
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
import java.util.Set;

public class ReportService {

  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final IngredientPriceRepository ingredientPriceRepository;
  private final IngredientReportGenerator ingredientReportGenerator;
  private final Restaurant restaurant;
  private final MaterialReportGenerator materialReportGenerator;
  private final ReportPeriodFactory reportPeriodFactory;
  private final ChefReportGenerator chefReportGenerator;
  private final ChefRepository chefRepository;

  public ReportService(IngredientQuantityRepository ingredientQuantityRepository,
                       IngredientPriceRepository ingredientPriceRepository,
                       IngredientReportGenerator ingredientReportGenerator,
                       Restaurant restaurant,
                       MaterialReportGenerator materialReportGenerator,
                       ReportPeriodFactory reportPeriodFactory,
                       ChefReportGenerator chefReportGenerator,
                       ChefRepository chefRepository)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.ingredientPriceRepository = ingredientPriceRepository;
    this.ingredientReportGenerator = ingredientReportGenerator;
    this.restaurant = restaurant;
    this.materialReportGenerator = materialReportGenerator;
    this.reportPeriodFactory = reportPeriodFactory;
    this.chefReportGenerator = chefReportGenerator;
    this.chefRepository = chefRepository;
  }

  public IngredientReport getIngredientReport(String startDate, String endDate) {
    ReportPeriod
      reportPeriod =
      reportPeriodFactory.create(LocalDate.parse(startDate), LocalDate.parse(endDate), restaurant.getHoppeningEvent().getDinnerPeriod());
    List<IngredientPriceDto> ingredientPrices = ingredientPriceRepository.getIngredientsPrice();
    Map<LocalDate, Map<IngredientName, BigDecimal>>
      dateToIngredientQuantities =
      ingredientQuantityRepository.getIngredientsQuantity(reportPeriod);
    return ingredientReportGenerator.generateReport(ingredientPrices, dateToIngredientQuantities);
  }

  public DinnerPeriodDto getDinnerPeriodDto() {
    return new DinnerPeriodDto(restaurant.getHoppeningEvent().getDinnerPeriod());
  }

  public MaterialReport getMaterialReport(String startDate, String endDate) {
    ReportPeriod
      reportPeriod =
      reportPeriodFactory.create(LocalDate.parse(startDate), LocalDate.parse(endDate), restaurant.getHoppeningEvent().getDinnerPeriod());
    Map<LocalDate, DailyDishesQuantity> dailyDishesQuantity = restaurant.getDailyDishesQuantity(reportPeriod);
    return materialReportGenerator.generateReport(dailyDishesQuantity, reportPeriod);
  }

  public ChefReport getChefReport() {
    Map<LocalDate, Set<Chef>> chefsByDate = chefRepository.getAllChefsWorkSchedule();
    return chefReportGenerator.generateReport(chefsByDate);
  }

  public ExpenseReport getExpenseReport() {
    String openingDate = restaurant.getHoppeningEvent().getDinnerPeriod().getStartDate().toString();
    String closingDate = restaurant.getHoppeningEvent().getDinnerPeriod().getEndDate().toString();

    MaterialReport materialReport = getMaterialReport(openingDate, closingDate);
    IngredientReport ingredientReport = getIngredientReport(openingDate, closingDate);
    ChefReport chefReport = getChefReport();


    ExpenseReport expenseReport = new ExpenseReport();
    expenseReport.addExpense(materialReport.calculateTotalCost());
    expenseReport.addExpense(ingredientReport.calculateTotalPriceForEntireReport());
    expenseReport.addExpense(chefReport.calculateTotalCost());
    expenseReport.addIncome(restaurant.calculateTotalReservationFee());
    return expenseReport;
  }
}
