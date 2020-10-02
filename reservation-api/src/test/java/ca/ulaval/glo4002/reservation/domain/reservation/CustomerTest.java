package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;

import java.math.BigDecimal;

public class CustomerTest {
  private static final RestrictionType A_RESTRICTION_WITHOUT_FEE = RestrictionType.ALLERGIES;
  private static final BigDecimal CUSTOMER_FEES_WITH_NO_RESTRICTION = BigDecimal.valueOf(1000);
  private static final BigDecimal CUSTOMER_FEES_WITH_VEGAN_RESTRICTION = BigDecimal.valueOf(2000);

  @Test
  public void givenCustomerWithoutRestrictions_whenGetCustomerFees_thenReturnBasePrice() {
    // given
    Customer customer = new CustomerBuilder().build();

    // when
    BigDecimal price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(CUSTOMER_FEES_WITH_NO_RESTRICTION);
  }

  @Test
  public void givenCustomerWithRestrictionWithoutFee_whenGetCustomerFees_thenReturnBasePrice() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(A_RESTRICTION_WITHOUT_FEE).build();

    // when
    BigDecimal price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(CUSTOMER_FEES_WITH_NO_RESTRICTION);
  }

  @Test
  public void givenCustomerWithRestriction_whenGetCustomerFees_thenReturnPriceAccordingToRestriction() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();

    // when
    BigDecimal price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(CUSTOMER_FEES_WITH_VEGAN_RESTRICTION);
  }

  @Test
  public void givenCustomerWithManyRestriction_whenGetCustomerFees_thenReturnPriceAccordingToRestrictions() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN)
                                             .withRestriction(RestrictionType.ILLNESS)
                                             .build();
    BigDecimal expectedPrice = Customer.BASIC_CUSTOMER_FEES.add(RestrictionType.VEGAN.getFees()).add(RestrictionType.ILLNESS.getFees());

    // when
    BigDecimal price = customer.getCustomerFees();

    // then
    assertThat(price).isEqualTo(CUSTOMER_FEES_WITH_VEGAN_RESTRICTION);
  }
}
