package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.api.report.dto.IngredientsReportInformationDto;
import ca.ulaval.glo4002.reservation.api.report.dto.TotalReportDto;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.IngredientReportInformation;
import ca.ulaval.glo4002.reservation.domain.report.total.TotalReport;
import ca.ulaval.glo4002.reservation.domain.report.unit.UnitReportDay;

@ExtendWith(MockitoExtension.class)
class TotalReportDtoAssemblerTest {
  private TotalReportDtoAssembler totalReportDtoAssembler;
  private static final BigDecimal REAL_TOTAL_PRICE = BigDecimal.valueOf(23.0);
  private static final IngredientName BUTTERNUT_SQUASH_NAME = IngredientName.BUTTERNUT_SQUASH;
  private static final IngredientName CHOCOLATE_NAME = IngredientName.CHOCOLATE;
  private static final BigDecimal A_QUANTITY = BigDecimal.valueOf(2.0);
  private static final BigDecimal ANOTHER_QUANTITY = BigDecimal.valueOf(3.0);
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;
  private static final BigDecimal ANOTHER_TOTAL_PRICE = BigDecimal.ONE;

  @Mock
  private UnitReportDay aReportDay;

  @Mock
  private TotalReport aReport;

  @Mock
  private IngredientsReportInformationDto anIngredientReportInformationDto;

  @Mock
  private IngredientsReportInformationDto anotherIngredientReportInformationDto;

  @Mock
  private IngredientReportInformationDtoAssembler ingredientReportInformationDtoAssembler;

  @BeforeEach
  public void setUp() {
    totalReportDtoAssembler = new TotalReportDtoAssembler(ingredientReportInformationDtoAssembler);
  }

  @Test
  public void givenReportWithOneReportDay_whenAssembleTotalReportDto_thenValidTotalReportDtoIsReturned() {
    // given
    Set<IngredientReportInformation> ingredientReportInformationSet = givenIngredientReportInformation();
    List<IngredientsReportInformationDto> list = givenAListOfIngredientReportInformationDtos();
    given(aReport.getTotalPrice()).willReturn(REAL_TOTAL_PRICE);
    given(aReport.getIngredientsReportInformation()).willReturn(ingredientReportInformationSet);
    given(ingredientReportInformationDtoAssembler.assembleFromIngredientReportInformations(ingredientReportInformationSet)).willReturn(list);

    // when
    TotalReportDto totalreportDto = totalReportDtoAssembler.assemble(aReport);

    // then
    assertThat(totalreportDto.getTotalPrice()
                             .doubleValue()).isEqualTo(REAL_TOTAL_PRICE.doubleValue());
    assertThat(totalreportDto.getIngredients()).isEqualTo(list);
  }

  private List<IngredientsReportInformationDto> givenAListOfIngredientReportInformationDtos() {
    return Arrays.asList(anIngredientReportInformationDto, anotherIngredientReportInformationDto);
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