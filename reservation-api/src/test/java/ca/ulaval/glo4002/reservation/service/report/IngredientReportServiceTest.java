package ca.ulaval.glo4002.reservation.service.report;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@ExtendWith(MockitoExtension.class)
public class IngredientReportServiceTest {
  private static final LocalDate START_LOCAL_DATE = LocalDate.of(2150, 7, 21);
  private static final LocalDate END_LOCAL_DATE = LocalDate.of(2150, 7, 29);

  private static final String REPORT_START_DATE = "2150-07-21";
  private static final String REPORT_END_DATE = "2150-07-29";

  private static final LocalDate A_DATE = LocalDate.of(2150, 7, 20);
  private static final IngredientName AN_INGREDIENT_NAME = IngredientName.BUTTERNUT_SQUASH;
  private static final BigDecimal A_QUANTITY = BigDecimal.valueOf(5.0);

  @Mock
  private IngredientQuantityRepository ingredientQuantityRepository;

  @Mock
  private IngredientPriceRepository ingredientPriceRepository;

  @Mock
  private ReportPeriod reportPeriod;

  @Mock
  private IngredientPriceDto anIngredientPriceDto;

  @Mock
  private IngredientReportGenerator ingredientReportGenerator;

  @Mock
  private Restaurant restaurant;

  @Mock
  private MaterialReportGenerator materialReportGenerator;

  @Mock
  private DailyDishesQuantity dailyDishesQuantity;

  @Mock
  private Map<LocalDate, DailyDishesQuantity> dailyDishesQuantities;

  @Mock
  private ReportPeriodFactory reportPeriodFactory;

  @Mock
  private Period dinnerPeriod;

  @Mock
  private HoppeningEvent hoppeningEvent;

  private IngredientReportService ingredientReportService;

  @BeforeEach
  public void setUpIngredientReportService() {
    dailyDishesQuantities.put(START_LOCAL_DATE, dailyDishesQuantity);
    ingredientReportService = new IngredientReportService(ingredientQuantityRepository,
                                                          ingredientPriceRepository,
                                                          ingredientReportGenerator,
                                                          restaurant,
                                                          materialReportGenerator,
                                                          reportPeriodFactory);
  }

  @BeforeEach
  public void setUpHoppeningEvent() {
    given(hoppeningEvent.getDinnerPeriod()).willReturn(dinnerPeriod);
    given(restaurant.getHoppeningEvent()).willReturn(hoppeningEvent);
  }

  @Test
  public void whenGetIngredientReport_thenIngredientsPriceAreRetrieved() {
    // when
    ingredientReportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientPriceRepository).getIngredientsPrice();
  }

  @Test
  public void whenGetIngredientReport_ThenIngredientsQuantityAreRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);

    // when
    ingredientReportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientQuantityRepository).getIngredientsQuantity(reportPeriod);
  }

  @Test
  public void whenGetIngredientReport_thenReportPeriodFactoryCreateIsCalled() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);

    // when
    ingredientReportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(reportPeriodFactory).create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod);
  }

  @Test
  public void givenIngredientPriceDtosAndIngredientsQuantity_whenGetIngredientReport_thenReportIsGenerated() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);
    List<IngredientPriceDto> ingredientPriceDtos = givenIngredientPriceDtos();
    given(ingredientPriceRepository.getIngredientsPrice()).willReturn(ingredientPriceDtos);

    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = givenIngredientsQuantity();
    given(ingredientQuantityRepository.getIngredientsQuantity(reportPeriod)).willReturn(ingredientsQuantity);

    // when
    ingredientReportService.getIngredientReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(ingredientReportGenerator).generateReport(ingredientPriceDtos, ingredientsQuantity);
  }

  @Test
  public void whenGetMaterialReport_thenMaterialReportIsRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);
    given(restaurant.getDailyDishesQuantity(reportPeriod)).willReturn(dailyDishesQuantities);

    // when
    ingredientReportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(materialReportGenerator).generateReport(dailyDishesQuantities, reportPeriod);
  }

  @Test
  public void whenGetMaterialReport_thenDailyDishesQuantityIsRetrieved() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);

    // when
    ingredientReportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(restaurant).getDailyDishesQuantity(reportPeriod);
  }

  @Test
  public void whenGetMaterialReport_thenReportPeriodFactoryCreateIsCalled() {
    // given
    given(reportPeriodFactory.create(START_LOCAL_DATE,
                                     END_LOCAL_DATE,
                                     dinnerPeriod)).willReturn(reportPeriod);

    // when
    ingredientReportService.getMaterialReport(REPORT_START_DATE, REPORT_END_DATE);

    // then
    verify(reportPeriodFactory).create(START_LOCAL_DATE, END_LOCAL_DATE, dinnerPeriod);
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
