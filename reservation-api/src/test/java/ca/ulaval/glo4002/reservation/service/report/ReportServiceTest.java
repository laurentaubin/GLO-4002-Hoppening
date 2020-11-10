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

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.material.Buffet;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.material.MaterialReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.IngredientPriceRepository;
import ca.ulaval.glo4002.reservation.domain.report.ReportGenerator;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

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
  private ReportGenerator reportGenerator;

  @Mock
  private MaterialReportGenerator materialReportGenerator;

  @Mock
  private Buffet buffet;

  @Mock
  private Map<LocalDate, DailyDishesQuantity> dailyDishesQuantities;

  private ReportService reportService;

  @BeforeEach
  public void setUp() {
    reportService = new ReportService(ingredientQuantityRepository,
                                      ingredientPriceRepository,
                                      reportGenerator,
                                      materialReportGenerator,
                                      buffet);
  }

  @Test
  public void whenGetReportResponse_thenIngredientsPriceAreRetrieved() {
    // when
    reportService.getIngredientReport(reportPeriod);

    // then
    verify(ingredientPriceRepository).getIngredientsPrice();
  }

  @Test
  public void whenGetMaterialReport_thenMaterialReportIsRetrieved() {
    // given
    given(buffet.getDailyDishesQuantities(reportPeriod)).willReturn(dailyDishesQuantities);

    // when
    reportService.getMaterialReport(reportPeriod);

    // then
    verify(materialReportGenerator).generateReport(dailyDishesQuantities, reportPeriod);
  }

  @Test
  public void whenGetMaterialReport_thenDishesFromBuffetAreRetrieved() {
    // given
    given(buffet.getDailyDishesQuantities(reportPeriod)).willReturn(dailyDishesQuantities);

    // when
    reportService.getMaterialReport(reportPeriod);

    // then
    verify(buffet).getDailyDishesQuantities(reportPeriod);
  }

  @Test
  public void whenGetReportResponse_ThenIngredientsQuantityAreRetrieved() {
    // when
    reportService.getIngredientReport(reportPeriod);

    // then
    verify(ingredientQuantityRepository).getIngredientsQuantity(reportPeriod);
  }

  @Test
  public void givenIngredientPriceDtosAndIngredientsQuantity_whenGetReportResponse_thenReportIsGenerated() {
    // given
    List<IngredientPriceDto> ingredientPriceDtos = givenIngredientPriceDtos();
    given(ingredientPriceRepository.getIngredientsPrice()).willReturn(ingredientPriceDtos);

    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = givenIngredientsQuantity();
    given(ingredientQuantityRepository.getIngredientsQuantity(reportPeriod)).willReturn(ingredientsQuantity);

    // when
    reportService.getIngredientReport(reportPeriod);

    // then
    verify(reportGenerator).generateReport(ingredientPriceDtos, ingredientsQuantity);
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
