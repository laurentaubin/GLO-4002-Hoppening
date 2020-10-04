package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.api.report.dto.UnitReportDayDto;
import ca.ulaval.glo4002.reservation.domain.report.UnitReportDay;

@ExtendWith(MockitoExtension.class)
public class UnitReportDayDtoAssemblerTest {
  private static final LocalDate A_DATE = LocalDate.of(2150, 7, 20);
  private static final String A_DATE_STRING = "2150-07-20";
  private static final LocalDate ANOTHER_DATE = LocalDate.of(2150, 7, 23);
  private static final String ANOTHER_DATE_STRING = "2150-07-23";
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;
  private static final BigDecimal ANOTHER_TOTAL_PRICE = BigDecimal.ONE;

  @Mock
  private IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler;

  @Mock
  private UnitReportDay aUnitReportDay;

  @Mock
  private UnitReportDay anotherUnitReportDay;

  @Mock
  private IngredientsReportInformationDto anIngredientReportInformationDto;

  @Mock
  private IngredientsReportInformationDto anotherIngredientReportInformationDto;

  @Mock
  private IngredientsReportInformationDto someIngredientReportInformationDto;

  private UnitReportDayDtoAssembler unitReportDayDtoAssembler;

  @BeforeEach
  public void setUp() {
    given(aUnitReportDay.getDate()).willReturn(A_DATE);
    given(aUnitReportDay.getTotalPrice()).willReturn(A_TOTAL_PRICE);
    given(anotherUnitReportDay.getDate()).willReturn(ANOTHER_DATE);
    given(anotherUnitReportDay.getTotalPrice()).willReturn(ANOTHER_TOTAL_PRICE);

    unitReportDayDtoAssembler = new UnitReportDayDtoAssembler(ingredientReportInformationDtoAssembler);
  }

  @Test
  public void givenUnitReportLines_whenAssembleUnitReportLineDtos_thenAssembleCorrespondingDtos() {
    // given
    List<IngredientsReportInformationDto> aListOfIngredientRepotInformationDtos = givenAListOfIngredientReportInformationDtos();
    given(ingredientReportInformationDtoAssembler.assemble(aUnitReportDay)).willReturn(aListOfIngredientRepotInformationDtos);
    List<IngredientsReportInformationDto> anotherListOfIngredientRepotInformationDtos = givenAnotherListOfIngredientReportInformationDtos();
    given(ingredientReportInformationDtoAssembler.assemble(anotherUnitReportDay)).willReturn(anotherListOfIngredientRepotInformationDtos);
    List<UnitReportDay> unitReportDays = Arrays.asList(aUnitReportDay, anotherUnitReportDay);

    // when
    List<UnitReportDayDto> unitReportDayDtos = unitReportDayDtoAssembler.assembleUnitReportDayDtos(unitReportDays);

    // then
    UnitReportDayDto firstUnitReportDayDto = unitReportDayDtos.get(0);
    assertThat(firstUnitReportDayDto.getDate()).isEqualTo(A_DATE_STRING);
    assertThat(firstUnitReportDayDto.getIngredients()).isEqualTo(aListOfIngredientRepotInformationDtos);
    assertThat(firstUnitReportDayDto.getTotalPrice()).isEqualTo(A_TOTAL_PRICE);

    UnitReportDayDto secondUnitReportDayDto = unitReportDayDtos.get(1);
    assertThat(secondUnitReportDayDto.getDate()).isEqualTo(ANOTHER_DATE_STRING);
    assertThat(secondUnitReportDayDto.getIngredients()).isEqualTo(anotherListOfIngredientRepotInformationDtos);
    assertThat(secondUnitReportDayDto.getTotalPrice()).isEqualTo(ANOTHER_TOTAL_PRICE);
  }

  private List<IngredientsReportInformationDto> givenAListOfIngredientReportInformationDtos() {
    return Arrays.asList(anIngredientReportInformationDto, anotherIngredientReportInformationDto);
  }

  private List<IngredientsReportInformationDto> givenAnotherListOfIngredientReportInformationDtos() {
    return Collections.singletonList(someIngredientReportInformationDto);
  }
}
