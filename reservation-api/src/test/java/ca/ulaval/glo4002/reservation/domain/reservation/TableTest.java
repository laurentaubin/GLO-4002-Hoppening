package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

@ExtendWith(MockitoExtension.class)
public class TableTest {
  private static final double NO_PRICE = 0;
  private static final double A_PRICE = 1000;
  private static final double ANOTHER_PRICE = 3000;
  private static final int TWO_VEGANS = 2;
  private static final int ONE_CUSTOMER_WITH_ALLERGIES = 1;
  private static final int ONE_CUSTOMER_WITH_NO_RESTRICTION = 1;

  @Mock
  private Customer aCustomer;

  @Mock
  private Customer anotherCustomer;

  @Test
  public void givenTableWithoutCustomer_whenGetPrice_thenReturnNoCost() {
    // given
    Table table = new TableBuilder().build();

    // when
    double price = table.getTableReservationFees();

    // then
    assertThat(price).isEqualTo(NO_PRICE);
  }

  @Test
  public void givenTableWithACustomer_whenGetPrice_thenReturnCustomerPrice() {
    // given
    given(aCustomer.getCustomerFees()).willReturn(A_PRICE);
    Table table = new TableBuilder().withCustomer(aCustomer).build();

    // when
    double price = table.getTableReservationFees();

    // then
    assertThat(price).isEqualTo(A_PRICE);
  }

  @Test
  public void givenTableWithCustomers_whenGetPrice_thenReturnTotalPrice() {
    // given
    given(aCustomer.getCustomerFees()).willReturn(A_PRICE);
    given(anotherCustomer.getCustomerFees()).willReturn(ANOTHER_PRICE);
    Table table = new TableBuilder().withCustomer(aCustomer).withCustomer(anotherCustomer).build();
    double expectedPrice = A_PRICE + ANOTHER_PRICE;

    // when
    double totalPrice = table.getTableReservationFees();

    // then
    assertThat(totalPrice).isEqualTo(expectedPrice);
  }

  @Test
  public void givenCustomersWithRestrictions_whenGetRestrictionTypeCount_thenReturnNumberOfCustomersPerRestriction() {
    // given
    Customer aVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Customer anotherVeganCustomer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN)
                                                         .build();
    Customer aCustomerWithAllergies = new CustomerBuilder().withRestriction(RestrictionType.ALLERGIES)
                                                           .build();
    Customer aCustomerWithNoRestriction = new CustomerBuilder().build();
    Table table = new TableBuilder().withCustomers(Arrays.asList(aVeganCustomer,
                                                                 anotherVeganCustomer,
                                                                 aCustomerWithAllergies,
                                                                 aCustomerWithNoRestriction))
                                    .build();

    // when
    Map<RestrictionType, Integer> map = table.getRestrictionTypeCount();

    // then
    assertThat(map.get(RestrictionType.VEGAN)).isEqualTo(TWO_VEGANS);
    assertThat(map.get(RestrictionType.ALLERGIES)).isEqualTo(ONE_CUSTOMER_WITH_ALLERGIES);
    assertThat(map.get(RestrictionType.NONE)).isEqualTo(ONE_CUSTOMER_WITH_NO_RESTRICTION);
  }
}
