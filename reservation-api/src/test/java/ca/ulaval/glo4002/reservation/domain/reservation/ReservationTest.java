package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {

  private static final BigDecimal NO_COST = BigDecimal.ZERO;
  private static final BigDecimal A_TABLE_EXPECTED_PRICE = BigDecimal.valueOf(2500);
  private static final BigDecimal ANOTHER_TABLE_EXPECTED_PRICE = BigDecimal.valueOf(2500);
  private static final BigDecimal PRICE_WITH_TWO_TABLES = BigDecimal.valueOf(5000);
  private static final Map<RestrictionType, Integer> TWO_VEGANS = Collections.singletonMap(RestrictionType.VEGAN,
                                                                                           2);

  @Mock
  private Table aTable;

  @Mock
  private Table anotherTable;

  @Test
  public void givenAReservationWithEmptyTable_whenGetReservationFees_thenReturnNoCost() {
    // given
    given(aTable.getTableReservationFees()).willReturn(NO_COST);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    BigDecimal price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(NO_COST);
  }

  @Test
  public void givenAReservationWithATable_whenGetReservationFees_thenReturnPriceAccordingToReservation() {
    // given
    given(aTable.getTableReservationFees()).willReturn(A_TABLE_EXPECTED_PRICE);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    BigDecimal price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(A_TABLE_EXPECTED_PRICE);
  }

  @Test
  public void givenAReservationWithManyTables_whenGetReservationFees_thenReturnPriceAccordingToReservation() {
    // given
    given(aTable.getTableReservationFees()).willReturn(A_TABLE_EXPECTED_PRICE);
    given(anotherTable.getTableReservationFees()).willReturn(ANOTHER_TABLE_EXPECTED_PRICE);
    Reservation reservation = new ReservationBuilder().withTable(aTable)
                                                      .withTable(anotherTable)
                                                      .build();

    // when
    BigDecimal price = reservation.getReservationFees();

    // then
    assertThat(price).isEqualTo(PRICE_WITH_TWO_TABLES);
  }

  @Test
  public void givenAReservationWithTwoVeganCustomers_whenGetRestrictionTypeCount_thenReturnRestrictionTypeCount() {
    // given
    given(aTable.getRestrictionTypeCount()).willReturn(TWO_VEGANS);
    Reservation reservation = new ReservationBuilder().withTable(aTable).build();

    // when
    Map<RestrictionType, Integer> restrictionTypeCount = reservation.getRestrictionTypeCount();

    // then
    assertThat(restrictionTypeCount.get(RestrictionType.VEGAN)).isEqualTo(2);
  }
}