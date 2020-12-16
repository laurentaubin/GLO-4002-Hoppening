package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.Available;
import ca.ulaval.glo4002.reservation.domain.fullcourse.stock.IngredientAvailabilityValidator;

@ExtendWith(MockitoExtension.class)
public class IngredientAvailabilityValidatorTest {

  private static final IngredientName TOMATO = IngredientName.TOMATO;

  private static final LocalDate DATE_OUTSIDE_TOMATO_AVAILABILITY_PERIOD = LocalDate.of(2150,
                                                                                        7,
                                                                                        23);

  private static final LocalDate OPENING_DATE = LocalDate.of(2150, 7, 20);

  private static final BigDecimal QUANTITY = BigDecimal.valueOf(20);

  @Mock
  private ReservationIngredientCalculator reservationIngredientCalculator;

  @Mock
  private Available anIngredientStock;

  @Mock
  private Reservation reservation;

  private IngredientAvailabilityValidator ingredientAvailabilityValidator;

  @BeforeEach
  public void setUp() {
    Set<Available> ingredientsStocks = new HashSet<>();
    ingredientsStocks.add(anIngredientStock);
    ingredientAvailabilityValidator = new IngredientAvailabilityValidator(reservationIngredientCalculator,
                                                                          ingredientsStocks);
  }

  @Test
  public void givenReservationWithUnavailableIngredient_whenIsIngredientAvailable_thenReservationIsNotAllowed() {
    // given
    given(reservation.getDinnerDate()).willReturn(DATE_OUTSIDE_TOMATO_AVAILABILITY_PERIOD.atStartOfDay());
    given(anIngredientStock.getIngredientName()).willReturn(TOMATO);
    given(anIngredientStock.isAvailable(reservation.getDinnerDate().toLocalDate(),
                                        OPENING_DATE)).willReturn(false);
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(Map.of(TOMATO,
                                                                                                            QUANTITY));

    // when
    boolean isReservationAllowed = this.ingredientAvailabilityValidator.areIngredientsAvailableForReservation(reservation,
                                                                                                              OPENING_DATE);

    // then
    assertThat(isReservationAllowed).isFalse();
  }

  @Test
  public void givenReservationWithAvailable_whenIsIngredientAvailable_thenReservationIsAllowed() {
    // given
    given(reservationIngredientCalculator.getReservationIngredientsQuantity(reservation)).willReturn(Collections.emptyMap());

    // when
    boolean isReservationAllowed = ingredientAvailabilityValidator.areIngredientsAvailableForReservation(reservation,
                                                                                                         OPENING_DATE);

    // then
    assertThat(isReservationAllowed).isTrue();
  }
}
