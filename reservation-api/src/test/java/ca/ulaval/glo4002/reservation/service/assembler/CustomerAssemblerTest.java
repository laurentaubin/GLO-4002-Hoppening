package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.Restriction;

class CustomerAssemblerTest {
  private static final String VEGETARIAN_RESTRICTION = "vegetarian";
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final String ALLERGIES_RESTRICTION = "allergies";
  private static final String ILLNESS_RESTRICTION = "illness";

  private CustomerAssembler customerAssembler;

  @BeforeEach
  public void setUp() {
    customerAssembler = new CustomerAssembler();

  }

  @Test
  public void whenAssembleFromCustomerDto_thenReturnValidCustomer() {
    // given
    CustomerDto customerDto = new CustomerDtoBuilder().withRestriction(VEGETARIAN_RESTRICTION)
                                                      .withRestriction(VEGAN_RESTRICTION)
                                                      .build();

    // when
    Customer actualCustomer = customerAssembler.assembleFromCustomerDto(customerDto);

    // then
    assertThat(actualCustomer.getName()).isEqualTo(customerDto.getName());
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((VEGETARIAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((VEGAN_RESTRICTION)));
  }

  @Test
  public void givenACustomerDtoWithAllRestrictions_whenAssembleFromCustomerDto_thenReturnValidCustomer() {
    // given
    CustomerDto customerDto = new CustomerDtoBuilder().withRestriction(VEGETARIAN_RESTRICTION)
                                                      .withRestriction(VEGAN_RESTRICTION)
                                                      .withRestriction(ALLERGIES_RESTRICTION)
                                                      .withRestriction(ILLNESS_RESTRICTION)
                                                      .build();

    // when
    Customer actualCustomer = customerAssembler.assembleFromCustomerDto(customerDto);

    // then
    assertThat(actualCustomer.getName()).isEqualTo(customerDto.getName());
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((VEGETARIAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((VEGAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((ALLERGIES_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName((ILLNESS_RESTRICTION)));
  }

  @Test
  public void givenACustomerDtoWithNoRestrictions_whenAssembleFromCustomerDto_thenReturnValidCustomer() {
    // given
    CustomerDto customerDto = new CustomerDtoBuilder().build();

    // when
    Customer actualCustomer = customerAssembler.assembleFromCustomerDto(customerDto);

    // then
    assertThat(actualCustomer.getName()).isEqualTo(customerDto.getName());
    assertThat(actualCustomer.getRestrictions()).hasSize(0);
  }

  @Test
  public void givenACustomerDtoWithDuplicateRestrictions_whenAssembleFromCustomerDto_thenRestrictionsOnlyAppearOnce() {
    // given
    CustomerDto customerDto = new CustomerDtoBuilder().withRestriction(ALLERGIES_RESTRICTION)
                                                      .withRestriction(ALLERGIES_RESTRICTION)
                                                      .withRestriction(ILLNESS_RESTRICTION)
                                                      .build();

    // when
    Customer actualCustomer = customerAssembler.assembleFromCustomerDto(customerDto);

    // then
    assertThat(actualCustomer.getRestrictions()).hasSize(2);
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName(ALLERGIES_RESTRICTION));
    assertThat(actualCustomer.getRestrictions()).contains(Restriction.valueOfHoppeningName(ILLNESS_RESTRICTION));
  }
}