package ca.ulaval.glo4002.reservation.domain.report;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.exception.IngredientNotFoundException;
import ca.ulaval.glo4002.reservation.infra.report.IngredientPriceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IngredientPriceCalculatorTest {

    private IngredientPriceCalculator ingredientPriceCalculator;

    @Mock
    private IngredientPriceDto ingredientPriceDto1;

    @Mock
    private IngredientPriceDto ingredientPriceDto2;

    private BigDecimal EXPECTED_TOTAL_PRICE = BigDecimal.valueOf(3.0).setScale(2, RoundingMode.HALF_UP);

    private BigDecimal EXPECTED_PRICE_PER_KG = BigDecimal.valueOf(1.0).setScale(2, RoundingMode.HALF_UP);

    private BigDecimal EXPECTED_INGREDIENT_PRICE = BigDecimal.valueOf(10.0).setScale(2, RoundingMode.HALF_UP);

    private IngredientName AN_INGREDIENT_NAME = IngredientName.BUTTERNUT_SQUASH;

    private String INVALID_INGREDIENT_NAME_IN_DTO = "sauce";

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
        BigDecimal actualTotalPrice = ingredientPriceCalculator.getTotalPrice(IngredientName.PORK_LOIN, 3.0);

        // then
        assertThat(actualTotalPrice).isEqualTo(EXPECTED_TOTAL_PRICE);
    }

    @Test
    public void givenListDtoIngredientsWithMissingIngredientName_whenGetTotalPrice_thenShouldThrowIngredientNotFoundException() {
        // given
        List<IngredientPriceDto> dtoList = givenTwoIngredientDtoList();
        ingredientPriceCalculator.generatePriceMapper(dtoList);

        // when
        Executable gettingTotalPrice = () -> ingredientPriceCalculator.getTotalPrice(IngredientName.BACON, 2.0);

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
        assertThat(ingredientPriceCalculator.getTotalPrice(AN_INGREDIENT_NAME, 1))
                                            .isEqualTo(EXPECTED_INGREDIENT_PRICE);
    }

    private List<IngredientPriceDto> givenTwoIngredientDtoList() {
        given(ingredientPriceDto1.getName()).willReturn(IngredientName.PORK_LOIN.toString());
        given(ingredientPriceDto1.getPricePerKg()).willReturn(EXPECTED_PRICE_PER_KG);
        given(ingredientPriceDto2.getName()).willReturn(AN_INGREDIENT_NAME.toString());
        given(ingredientPriceDto2.getPricePerKg()).willReturn(EXPECTED_INGREDIENT_PRICE);

        return List.of(ingredientPriceDto1, ingredientPriceDto2);
    }

    private List<IngredientPriceDto> givenInvalidDtoList() {
        given(ingredientPriceDto1.getName()).willReturn(INVALID_INGREDIENT_NAME_IN_DTO);
        given(ingredientPriceDto2.getName()).willReturn(AN_INGREDIENT_NAME.toString());
        given(ingredientPriceDto2.getPricePerKg()).willReturn(EXPECTED_INGREDIENT_PRICE);

        return List.of(ingredientPriceDto1, ingredientPriceDto2);
    }


}