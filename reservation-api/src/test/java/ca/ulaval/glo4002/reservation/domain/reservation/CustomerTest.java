package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;

public class CustomerTest {
  private static final RestrictionType A_RESTRICTION_WITHOUT_FEE = RestrictionType.ALLERGIES;

  @Test
  public void givenCustomerWithoutRestrictions_whenGetCustomerFees_thenReturnBasePrice() {
    // given
    Customer customer = new CustomerBuilder().build();

    // when
    double price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(Customer.BASIC_CUSTOMER_FEES);
  }

  @Test
  public void givenCustomerWithRestrictionWithoutFee_whenGetCustomerFees_thenReturnBasePrice() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(A_RESTRICTION_WITHOUT_FEE).build();

    // when
    double price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(Customer.BASIC_CUSTOMER_FEES);
  }

  @Test
  public void givenCustomerWithRestriction_whenGetCustomerFees_thenReturnPriceAccordingToRestriction() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    double expectedPrice = Customer.BASIC_CUSTOMER_FEES + RestrictionType.VEGAN.getFees();

    // when
    double price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(expectedPrice);
  }

  @Test
  public void givenCustomerWithManyRestriction_whenGetCustomerFees_thenReturnPriceAccordingToRestrictions() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN)
                                             .withRestriction(RestrictionType.ILLNESS)
                                             .build();
    double expectedPrice = Customer.BASIC_CUSTOMER_FEES + RestrictionType.VEGAN.getFees()
                           + RestrictionType.ILLNESS.getFees();

    // when
    double price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(expectedPrice);
  }
}
