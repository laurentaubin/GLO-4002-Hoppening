package ca.ulaval.glo4002.reservation.domain;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {

  private static final double NO_COST = 0;
  private static final double A_PRICE = 2500;
  private static final double ANOTHER_PRICE = 2500;

  @Mock
  private Table aTable;

  @Mock
  Table anotherTable;

  @Test
  public void givenAReservationWithEmptyTable_whenGetReservationFees_thenReturnNoCost() {
    // given
    given(aTable.getTableReservationFees()).willReturn(NO_COST);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    double price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(NO_COST);
  }

  @Test
  public void givenAReservationWithATable_whenGetReservationFees_thenReturnPriceAccordingToReservation() {
    // given
    given(aTable.getTableReservationFees()).willReturn(A_PRICE);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    double price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(A_PRICE);
  }

  @Test
  public void givenAReservationWithManyTables_whenGetReservationFees_thenReturnPriceAccordingToReservation() {
    // given
    given(aTable.getTableReservationFees()).willReturn(A_PRICE);
    given(anotherTable.getTableReservationFees()).willReturn(ANOTHER_PRICE);
    Reservation reservation = new ReservationBuilder().withTable(aTable)
                                                      .withTable(anotherTable)
                                                      .build();
    double expectedPrice = A_PRICE + ANOTHER_PRICE;

    // when
    double price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(expectedPrice);
  }
}