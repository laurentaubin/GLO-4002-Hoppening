package ca.ulaval.glo4002.reservation.domain.report;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.exception.IngredientNotFoundException;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;

@ExtendWith(MockitoExtension.class)
class IngredientPriceCalculatorTest {
  private static final BigDecimal EXPECTED_TOTAL_PRICE = BigDecimal.valueOf(3.0);
  private static final BigDecimal EXPECTED_PRICE_PER_KG = BigDecimal.valueOf(1.0);
  private static final BigDecimal EXPECTED_INGREDIENT_PRICE = BigDecimal.valueOf(10.0);
  private static final IngredientName AN_INGREDIENT_NAME = IngredientName.BUTTERNUT_SQUASH;
  private static final String INVALID_INGREDIENT_NAME_IN_DTO = "sauce";

  private IngredientPriceCalculator ingredientPriceCalculator;

  @Mock
  private IngredientPriceDto ingredientPriceDto1;

  @Mock
  private IngredientPriceDto ingredientPriceDto2;

  @BeforeEach
  public void setUp() {
    ingredientPriceCalculator = new IngredientPriceCalculator();
  }

  @Test
  public void givenListDtoIngredients_whenGetTotalPrice_thenReturnCorrectValue() {
    // given
    List<IngredientPriceDto> dtoList = givenTwoIngredientDtoList();
    ingredientPriceCalculator.generatePriceMapper(dtoList);

    // when
    BigDecimal actualTotalPrice = ingredientPriceCalculator.getTotalPrice(IngredientName.PORK_LOIN,
                                                                          BigDecimal.valueOf(3.0));

    // then
    assertThat(actualTotalPrice.doubleValue()).isEqualTo(EXPECTED_TOTAL_PRICE.doubleValue());
  }

  @Test
  public void givenListDtoIngredientsWithMissingIngredientName_whenGetTotalPrice_thenShouldThrowIngredientNotFoundException() {
    // given
    List<IngredientPriceDto> dtoList = givenTwoIngredientDtoList();
    ingredientPriceCalculator.generatePriceMapper(dtoList);

    // when
    Executable gettingTotalPrice = () -> ingredientPriceCalculator.getTotalPrice(IngredientName.BACON,
                                                                                 BigDecimal.valueOf(2.0));

    // then
    assertThrows(IngredientNotFoundException.class, gettingTotalPrice);
  }

  @Test
  public void givenInvalidEntryDtoList_whenGenerateArchive_thenShouldIgnoreInvalidEntryAndGenerateMapper() {
    // given
    List<IngredientPriceDto> dtoList = givenInvalidDtoList();

    // when
    ingredientPriceCalculator.generatePriceMapper(dtoList);

    // then
    assertThat(ingredientPriceCalculator.getTotalPrice(AN_INGREDIENT_NAME,
                                                       BigDecimal.valueOf(1))).isEqualTo(EXPECTED_INGREDIENT_PRICE);
  }

  private List<IngredientPriceDto> givenTwoIngredientDtoList() {
    given(ingredientPriceDto1.getName()).willReturn(IngredientName.PORK_LOIN.toString());
    given(ingredientPriceDto1.getPricePerKg()).willReturn(EXPECTED_PRICE_PER_KG);
    given(ingredientPriceDto2.getName()).willReturn(AN_INGREDIENT_NAME.toString());
    given(ingredientPriceDto2.getPricePerKg()).willReturn(BigDecimal.TEN);

    return List.of(ingredientPriceDto1, ingredientPriceDto2);
  }

  private List<IngredientPriceDto> givenInvalidDtoList() {
    given(ingredientPriceDto1.getName()).willReturn(INVALID_INGREDIENT_NAME_IN_DTO);
    given(ingredientPriceDto2.getName()).willReturn(AN_INGREDIENT_NAME.toString());
    given(ingredientPriceDto2.getPricePerKg()).willReturn(BigDecimal.valueOf(10.0));

    return List.of(ingredientPriceDto1, ingredientPriceDto2);
  }

}