package ca.ulaval.glo4002.reservation.api.report.assembler;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.report.dto.ChefReportDto;
import ca.ulaval.glo4002.reservation.domain.chef.Chef;
import ca.ulaval.glo4002.reservation.domain.chef.ChefType;
import ca.ulaval.glo4002.reservation.domain.report.chef.ChefReport;

public class ChefReportDtoAssemblerTest {
  private static final String A_DATE = "2020-02-10";
  private static final String ANOTHER_DATE = "2040-02-10";
  private static final BigDecimal A_TOTAL_PRICE = BigDecimal.TEN;
  private static final BigDecimal ANOTHER_TOTAL_PRICE = BigDecimal.ONE;
  private static final ChefType A_CHEF_TYPE = ChefType.BOB_SMARTIES;
  private static final ChefType ANOTHER_CHEF_TYPE = ChefType.ERIC_ARDO;

  private ChefReportDtoAssembler chefReportDtoAssembler;
  private Chef aChef;
  private Chef anotherChef;

  @BeforeEach
  public void setUpChefReportDtoAssembler() {
    chefReportDtoAssembler = new ChefReportDtoAssembler();
    aChef = new Chef(A_CHEF_TYPE);
    anotherChef = new Chef(ANOTHER_CHEF_TYPE);
  }

  @Test
  public void givenChefsHiredOnTwoDates_whenAssembleChefReportDto_thenDtoHasMultipleChefInformationDtos() {
    // given
    ChefReport chefReport = givenAChefReportWithChefsHiredOnTwoDates();

    // when
    ChefReportDto chefReportDto = chefReportDtoAssembler.assembleChefReportDto(chefReport);

    // then
    assertThat(chefReportDto.getChefsReportInformationDto()
                            .size()).isEqualTo(chefReport.getChefReportInformation().size());
  }

  @Test
  public void givenChefsHiredOnOneDate_whenAssembleChefReportDto_thenDtoHasTheRightDate() {
    // given
    ChefReport chefReport = givenAChefReportWithChefsHiredOnOneDate();
    String expectedDate = chefReport.getChefReportInformation().get(0).getDate();

    // when
    ChefReportDto chefReportDto = chefReportDtoAssembler.assembleChefReportDto(chefReport);

    // then
    assertThat(chefReportDto.getChefsReportInformationDto()
                            .get(0)
                            .getDate()).isEqualTo(expectedDate);
  }

  @Test
  public void givenChefsHiredOnOneDate_whenAssembleChefReportDto_thenDtoHasTheRightPrice() {
    // given
    ChefReport chefReport = givenAChefReportWithChefsHiredOnOneDate();
    BigDecimal expectedTotalPrice = chefReport.getChefReportInformation().get(0).getTotalPrice();

    // when
    ChefReportDto chefReportDto = chefReportDtoAssembler.assembleChefReportDto(chefReport);

    // then
    assertThat(chefReportDto.getChefsReportInformationDto()
                            .get(0)
                            .getTotalPrice()).isEqualTo(expectedTotalPrice);
  }

  @Test
  public void givenChefsHiredOnOneDate_whenAssembleChefReportDto_thenDtoHasRightNumberOfChefs() {
    // given
    ChefReport chefReport = givenAChefReportWithMultipleChefs();
    Set<Chef> expectedChefs = chefReport.getChefReportInformation().get(0).getChefs();

    // when
    ChefReportDto chefReportDto = chefReportDtoAssembler.assembleChefReportDto(chefReport);

    // then
    Set<String> actualChefsName = chefReportDto.getChefsReportInformationDto().get(0).getChefs();
    assertThat(actualChefsName.size()).isEqualTo(expectedChefs.size());
  }

  private ChefReport givenAChefReportWithChefsHiredOnOneDate() {
    ChefReport chefReport = new ChefReport();
    chefReport.addChefReportInformation(A_DATE, Set.of(aChef), A_TOTAL_PRICE);
    return chefReport;
  }

  private ChefReport givenAChefReportWithMultipleChefs() {
    ChefReport chefReport = new ChefReport();
    chefReport.addChefReportInformation(A_DATE, Set.of(aChef, anotherChef), A_TOTAL_PRICE);
    return chefReport;
  }

  private ChefReport givenAChefReportWithChefsHiredOnTwoDates() {
    ChefReport chefReport = new ChefReport();
    chefReport.addChefReportInformation(A_DATE, Set.of(aChef), A_TOTAL_PRICE);
    chefReport.addChefReportInformation(ANOTHER_DATE, Set.of(aChef), ANOTHER_TOTAL_PRICE);
    return chefReport;
  }

}