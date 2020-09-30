package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class CustomerRestrictionTest {

  private static final double NO_ADDITIONAL_FEE = 0;

  @Test
  public void givenNoRestriction_whenGetAdditionalFees_thenReturnNoAdditionalFees() {
    // given
    Set<RestrictionType> noRestriction = Collections.emptySet();
    CustomerRestriction restriction = new CustomerRestriction(noRestriction);

    // when
    double price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(NO_ADDITIONAL_FEE);
  }

  @Test
  public void givenOneRestriction_whenGetAdditionalFees_thenReturnsPriceAccordingToRestriction() {
    // given
    Set<RestrictionType> veganRestriction = Set.of(RestrictionType.VEGAN);
    CustomerRestriction restriction = new CustomerRestriction(veganRestriction);

    // when
    double price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(RestrictionType.VEGAN.getFees());
  }

  @Test
  public void givenManyRestrictions_whenGetAdditionalFees_thenReturnsPriceAccordingToRestrictions() {
    // given
    Set<RestrictionType> veganRestriction = Set.of(RestrictionType.VEGAN, RestrictionType.ILLNESS);
    CustomerRestriction restriction = new CustomerRestriction(veganRestriction);

    double expectedPrice = RestrictionType.VEGAN.getFees() + RestrictionType.ILLNESS.getFees();

    // when
    double price = restriction.getAdditionalFees();

    // then
    assertThat(price).isEqualTo(expectedPrice);
  }
}
