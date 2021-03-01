package ca.ulaval.glo4002.reservation.service.assembler;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.api.reservation.builder.CustomerDtoBuilder;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.RestrictionType;
import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;

class CustomerAssemblerTest {
  private static final String VEGETARIAN_RESTRICTION = "vegetarian";
  private static final String VEGAN_RESTRICTION = "vegan";
  private static final String ALLERGIES_RESTRICTION = "allergies";
  private static final String ILLNESS_RESTRICTION = "illness";
  private static final RestrictionType VEGETARIAN_ACTUAL_RESTRICTION = RestrictionType.VEGETARIAN;
  private static final RestrictionType VEGAN_ACTUAL_RESTRICTION = RestrictionType.VEGAN;

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
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((VEGETARIAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((VEGAN_RESTRICTION)));
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
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((VEGETARIAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((VEGAN_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((ALLERGIES_RESTRICTION)));
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName((ILLNESS_RESTRICTION)));
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
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName(ALLERGIES_RESTRICTION));
    assertThat(actualCustomer.getRestrictions()).contains(RestrictionType.valueOfName(ILLNESS_RESTRICTION));
  }

  @Test
  public void whenAssembleDtoFromCustomer_thenReturnValidCustomerDto() {

    // given
    Customer customer = new CustomerBuilder().withRestriction(VEGETARIAN_ACTUAL_RESTRICTION)
                                             .withRestriction(VEGAN_ACTUAL_RESTRICTION)
                                             .build();

    // when
    CustomerDto actualCustomerDto = customerAssembler.assembleDtoFromCustomer(customer);

    // then
    assertThat(actualCustomerDto.getName()).isEqualTo(customer.getName());
    assertThat(actualCustomerDto.getRestrictions()).contains(VEGETARIAN_ACTUAL_RESTRICTION.toString());
    assertThat(actualCustomerDto.getRestrictions()).contains(VEGAN_ACTUAL_RESTRICTION.toString());
  }

  @Test
  public void givenACustomerWithNoRestrictions_whenAssembleDtoFromCustomer_thenReturnValidCustomer() {
    // given
    Customer customer = new CustomerBuilder().build();

    // when
    CustomerDto actualCustomerDto = customerAssembler.assembleDtoFromCustomer(customer);

    // then
    assertThat(actualCustomerDto.getName()).isEqualTo(customer.getName());
    assertThat(actualCustomerDto.getRestrictions()).hasSize(0);
  }

  @Test
  public void givenACustomerWithUnorderedRestrictions_whenAssembleDtoFromCustomer_thenReturnCustomerWithRestrictionInOrder() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN)
                                             .withRestriction(RestrictionType.ILLNESS)
                                             .build();

    // when
    CustomerDto actualCustomerDto = customerAssembler.assembleDtoFromCustomer(customer);

    // then
    assertThat(actualCustomerDto.getRestrictions()
                                .get(0)).isEqualTo(RestrictionType.ILLNESS.toString());
    assertThat(actualCustomerDto.getRestrictions()
                                .get(1)).isEqualTo(RestrictionType.VEGAN.toString());
  }
}
