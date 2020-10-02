package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CustomerRestrictionTest {

  private static final BigDecimal NO_ADDITIONAL_FEE = BigDecimal.ZERO;
  private static final BigDecimal VEGAN_RESTRICTION_EXPECTED_PRICE = BigDecimal.valueOf(1000);

  @Test
  public void givenNoRestriction_whenGetAdditionalFees_thenReturnNoAdditionalFees() {
    // given
    Set<RestrictionType> noRestriction = Collections.emptySet();
    CustomerRestriction restriction = new CustomerRestriction(noRestriction);

    // when
    BigDecimal price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(NO_ADDITIONAL_FEE);
  }

  @Test
  public void givenOneRestriction_whenGetAdditionalFees_thenReturnsPriceAccordingToRestriction() {
    // given
    Set<RestrictionType> veganRestriction = Set.of(RestrictionType.VEGAN);
    CustomerRestriction restriction = new CustomerRestriction(veganRestriction);

    // when
    BigDecimal price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(VEGAN_RESTRICTION_EXPECTED_PRICE);
  }

  @Test
  public void givenManyRestrictions_whenGetAdditionalFees_thenReturnsPriceAccordingToRestrictions() {
    // given
    Set<RestrictionType> veganRestriction = Set.of(RestrictionType.VEGAN, RestrictionType.ILLNESS);
    CustomerRestriction restriction = new CustomerRestriction(veganRestriction);

    // when
    BigDecimal price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(VEGAN_RESTRICTION_EXPECTED_PRICE);
  }
}
