package ca.ulaval.glo4002.reservation.domain.reservation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.ulaval.glo4002.reservation.domain.builder.CustomerBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.ReservationBuilder;
import ca.ulaval.glo4002.reservation.domain.builder.TableBuilder;
import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.inmemory.MenuRepository;

@ExtendWith(MockitoExtension.class)
class ReservationIngredientCalculatorTest {

  private ReservationIngredientCalculator reservationIngredientCalculator;

  @Mock
  MenuRepository menuRepository;

  @BeforeEach
  public void setUp() {
    reservationIngredientCalculator = new ReservationIngredientCalculator(menuRepository);
  }

  @Test
  public void whenGetReservationIngredientsQuantity_thenReturnIngredientsQuantity() {
    // given
    Customer customer = new CustomerBuilder().withRestriction(RestrictionType.VEGAN).build();
    Table table = new TableBuilder().withCustomer(customer).build();
    Reservation reservation = new ReservationBuilder().withTable(table).build();
    Map<IngredientName, Double> veganMenu = givenVeganCourseIngredientsQuantity();
    given(menuRepository.getIngredientsQuantity(RestrictionType.VEGAN)).willReturn(veganMenu);

    // when
    Map<IngredientName, Double> ingredientQuantity = reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);

    // then
    assertThat(ingredientQuantity).isEqualTo(veganMenu);
  }

  private Map<IngredientName, Double> givenVeganCourseIngredientsQuantity() {
    Map<IngredientName, Double> ingredientDoubleMap = new HashMap<>();
    ingredientDoubleMap.put(IngredientName.TOMATO, 5.0);
    ingredientDoubleMap.put(IngredientName.KIWI, 8.0);
    ingredientDoubleMap.put(IngredientName.WORCESTERSHIRE_SAUCE, 5.0);
    ingredientDoubleMap.put(IngredientName.KIMCHI, 10.0);
    return ingredientDoubleMap;
  }
}
