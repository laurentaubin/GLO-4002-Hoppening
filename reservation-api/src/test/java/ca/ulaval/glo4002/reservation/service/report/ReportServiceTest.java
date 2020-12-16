package ca.ulaval.glo4002.reservation.service.report;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReport;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReport;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReport;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefRepository;
import ca.ulaval.glo4002.reservation.domain.report.expense.ExpenseReport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.Restaurant;
import ca.ulaval.glo4002.reservation.domain.date.Period;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.IngredientPriceRepository;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriodFactory;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReportServiceTest {
  private static final LocalDate START_LOCAL_DATE = LocalDate.of(2150, 7, 21);
  private static final LocalDate END_LOCAL_DATE = LocalDate.of(2150, 7, 29);

  private static final String REPORT_START_DATE = "2150-07-21";
  private static final String REPORT_END_DATE = "2150-07-29";

  private static final LocalDate A_DATE = LocalDate.of(2150, 7, 20);
  private static final IngredientName AN_INGREDIENT_NAME = IngredientName.BUTTERNUT_SQUASH;
  private static final BigDecimal A_QUANTITY = BigDecimal.valueOf(5.0);
  private static final BigDecimal CHEF_COST = BigDecimal.ONE;
  private static final BigDecimal MATERIAL_COST = BigDecimal.TEN;
  private static final BigDecimal INGREDIENT_COST = BigDecimal.ZERO;
  private static final BigDecimal RESTAURANT_INCOME = BigDecimal.valueOf(12);

  @Mock private IngredientQuantityRepository ingredientQuantityRepository;

  @Mock private IngredientPriceRepository ingredientPriceRepository;

  @Mock private ReportPeriod reportPeriod;

  @Mock private IngredientPriceDto anIngredientPriceDto;

  @Mock private IngredientReportGenerator ingredientReportGenerator;

  @Mock private ChefReportGenerator chefReportGenerator;

  @Mock private ChefRepository chefRepository;

  @Mock private Restaurant restaurant;

  @Mock private MaterialReportGenerator materialReportGenerator;

  @Mock private DailyDishesQuantity dailyDishesQuantity;

  @Mock private Map<LocalDate, DailyDishesQuantity> dailyDishesQuantities;

  @Mock private ReportPeriodFactory reportPeriodFactory;

  @Mock private Period dinnerPeriod;

  @Mock private HoppeningEvent hoppeningEvent;

  @Mock
  private ChefReport chefReport;

  @Mock
  private MaterialReport materialReport;

  @Mock
  private IngredientReport ingredientReport;

  private ReportService reportService;

  @BeforeEach public void setUpIngredientReportService() {
    dailyDishesQuantities.put(START_LOCAL_DATE, dailyDishesQuantity);
    reportService =
      new ReportService(ingredientQuantityRepository,
                        ingredientPriceRepository,
                        ingredientReportGenerator,
                        restaurant,
                        materialReportGenerator,
                        reportPeriodFactory,
                        chefReportGenerator,
                        chefRepository);
  }

  @BeforeEach public void setUpHoppeningEvent() {
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(restaurant.getHoppeningEvent()).willReturn(hoppeningEvent);
  }

  @Test public void whenGetIngredientReport_thenIngredientsPriceAreRetrieved() {
    // when
    reportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientPriceRepository).getIngredientsPrice();
  }

  @Test public void whenGetIngredientReport_ThenIngredientsQuantityAreRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);

    // when
    reportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientQuantityRepository).getIngredientsQuantity(reportPeriod);
  }

  @Test public void whenGetIngredientReport_thenReportPeriodFactoryCreateIsCalled() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);

    // when
    reportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(reportPeriodFactory).create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod);
  }

  @Test public void givenIngredientPriceDtosAndIngredientsQuantity_whenGetIngredientReport_thenReportIsGenerated() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);
    List<IngredientPriceDto> ingredientPriceDtos = givenIngredientPriceDtos();
    given(ingredientPriceRepository.getIngredientsPrice()).willReturn(ingredientPriceDtos);

    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = givenIngredientsQuantity();
    given(ingredientQuantityRepository.getIngredientsQuantity(reportPeriod)).willReturn(ingredientsQuantity);

    // when
    reportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientReportGenerator).generateReport(ingredientPriceDtos, ingredientsQuantity);
  }

  @Test public void whenGetMaterialReport_thenMaterialReportIsRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);
    given(restaurant.getDailyDishesQuantity(reportPeriod)).willReturn(dailyDishesQuantities);

    // when
    reportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(materialReportGenerator).generateReport(dailyDishesQuantities, reportPeriod);
  }

  @Test public void whenGetMaterialReport_thenDailyDishesQuantityIsRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);

    // when
    reportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(restaurant).getDailyDishesQuantity(reportPeriod);
  }

  @Test public void whenGetMaterialReport_thenReportPeriodFactoryCreateIsCalled() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod)).willReturn(reportPeriod);

    // when
    reportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(reportPeriodFactory).create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod);
  }

  @Test public void whenGetChefReport_thenChefReportIsCreated() {
    // given
    Map<LocalDate, Set<Chef>> chefsByDate = Collections.emptyMap();
    given(chefRepository.getAllChefsWorkSchedule()).willReturn(chefsByDate);
    // when
    reportService.getChefReport();

    // then
    verify(chefReportGenerator).generateReport(chefsByDate);
  }

  @Test
  public void whenGetExpenseReport_thenExpenseReportIsCreated() {
    // given
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(dinnerPeriod.getEndDate()).willReturn(END_LOCAL_DATE);
    given(dinnerPeriod.getStartDate()).willReturn(START_LOCAL_DATE);

    given(chefReportGenerator.generateReport(any())).willReturn(chefReport);
    given(materialReportGenerator.generateReport(any(), any())).willReturn(materialReport);
    given(ingredientReportGenerator.generateReport(any(), any())).willReturn(ingredientReport);
    given(chefReport.calculateTotalCost()).willReturn(CHEF_COST);
    given(materialReport.calculateTotalCost()).willReturn(MATERIAL_COST);
    given(ingredientReport.calculateTotalPriceForEntireReport()).willReturn(INGREDIENT_COST);
    given(restaurant.calculateTotalReservationFee()).willReturn(RESTAURANT_INCOME);

    // when
    ExpenseReport expenseReport = reportService.getExpenseReport();

    // then
    assertThat(expenseReport.getExpense()).isEqualTo(CHEF_COST.add(MATERIAL_COST).add(INGREDIENT_COST));
    assertThat(expenseReport.getIncome()).isEqualTo(RESTAURANT_INCOME);
  }

  private List<IngredientPriceDto> givenIngredientPriceDtos() {
    return Collections.singletonList(anIngredientPriceDto);
  }

  private Map<LocalDate, Map<IngredientName, BigDecimal>> givenIngredientsQuantity() {
    Map<IngredientName, BigDecimal> ingredientNameToQuantity = new HashMap<>();
    ingredientNameToQuantity.put(AN_INGREDIENT_NAME, A_QUANTITY);

    Map<LocalDate, Map<IngredientName, BigDecimal>> dateToIngredientsQuantity = new HashMap<>();
    dateToIngredientsQuantity.put(A_DATE, ingredientNameToQuantity);
    return dateToIngredientsQuantity;
  }
}
