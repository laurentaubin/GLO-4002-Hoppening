package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportDay;

@ExtendWith(MockitoExtension.class)
public class IngredientReportInformationDtoAssemblerTest {
  private static final IngredientName BUTTERNUT_SQUASH_NAME = IngredientName.BUTTERNUT_SQUASH;
  private static final IngredientName CHOCOLATE_NAME = IngredientName.CHOCOLATE;
  private static final BigDecimal A_QUANTITY = BigDecimal.valueOf(2.0);
  private static final BigDecimal ANOTHER_QUANTITY = BigDecimal.valueOf(3.0);
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;
  private static final BigDecimal ANOTHER_TOTAL_PRICE = BigDecimal.ONE;

  private IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler;

  @Mock
  private UnitReportDay unitReportDay;

  @BeforeEach
  public void setUp() {
    ingredientReportInformationDtoAssembler = new IngredientReportInformationDtoAssembler();
  }

  @Test
  public void givenAUnitReportLineWithTwoIngredientReportInformation_whenAssemble_thenAssembleTwoIngredientReportInformationDto() {
    // given
    given(unitReportDay.getIngredientsReportInformation()).willReturn(givenIngredientReportInformation());

    // when
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(unitReportDay.getIngredientsReportInformation());

    // then
    assertThat(ingredientsReportInformationDtos.size()).isEqualTo(2);
  }

  @Test
  public void givenAUnitReportLineWithTwoIngredientReportInformations_whenAssemble_thenIngredientsReportInformationDtosAreInAlphabeticOrder() {
    // given
    given(unitReportDay.getIngredientsReportInformation()).willReturn(givenIngredientReportInformation());

    // when
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(unitReportDay.getIngredientsReportInformation());

    // then
    IngredientsReportInformationDto firstIngredientReportInformation = ingredientsReportInformationDtos.get(0);
    IngredientsReportInformationDto secondIngredientReportInformation = ingredientsReportInformationDtos.get(1);
    assertThat(firstIngredientReportInformation.getIngredientName()).isEqualTo(BUTTERNUT_SQUASH_NAME.toString());
    assertThat(secondIngredientReportInformation.getIngredientName()).isEqualTo(CHOCOLATE_NAME.toString());
  }

  @Test
  public void givenIngredientReportInformation_whenAssembleFromIngredientReportInformations_thenAssemble() {
    // when
    List<IngredientsReportInformationDto> ingredientsReportInformationDtos = ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(givenIngredientReportInformation());

    // then
    IngredientsReportInformationDto firstIngredientReportInformation = ingredientsReportInformationDtos.get(0);
    IngredientsReportInformationDto secondIngredientReportInformation = ingredientsReportInformationDtos.get(1);
    assertThat(firstIngredientReportInformation.getIngredientName()).isEqualTo(BUTTERNUT_SQUASH_NAME.toString());
    assertThat(secondIngredientReportInformation.getIngredientName()).isEqualTo(CHOCOLATE_NAME.toString());
  }

  private Set<IngredientReportInformation> givenIngredientReportInformation() {
    IngredientReportInformation anIngredientReportInformation = new IngredientReportInformation(BUTTERNUT_SQUASH_NAME,
                                                                                                A_QUANTITY,
                                                                                                A_TOTAL_PRICE);
    IngredientReportInformation anotherIngredientReportInformation = new IngredientReportInformation(CHOCOLATE_NAME,
                                                                                                     ANOTHER_QUANTITY,
                                                                                                     ANOTHER_TOTAL_PRICE);
    return Set.of(anIngredientReportInformation, anotherIngredientReportInformation);
  }
}
