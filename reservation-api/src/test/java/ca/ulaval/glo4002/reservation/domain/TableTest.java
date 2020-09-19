package ca.ulaval.glo4002.reservation.domain;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;

@ExtendWith(MockitoExtension.class)
public class TableTest {
  private static final double NO_PRICE = 0;
  private static final double A_PRICE = 1000;
  private static final double ANOTHER_PRICE = 3000;

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
}
