package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.fullcourse.MenuRepository;
import ca.ulaval.glo4002.reservation.domain.util.MapUtil;

public class ReservationIngredientCalculator {
  private final MenuRepository menuRepository;

  public ReservationIngredientCalculator(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public Map<IngredientName, BigDecimal> getReservationIngredientsQuantity(
      Reservation reservation) {
    Map<RestrictionType, Integer> reservationRestrictionTypeCount =
        reservation.getRestrictionTypeCount();
    Map<IngredientName, BigDecimal> reservationIngredientsQuantity = new HashMap<>();

    for (Map.Entry<RestrictionType, Integer> restrictionTypeCount : reservationRestrictionTypeCount
        .entrySet()) {
      Map<IngredientName, BigDecimal> ingredientsQuantity =
          menuRepository.getIngredientsQuantityByRestrictionType(restrictionTypeCount.getKey());
      for (Map.Entry<IngredientName, BigDecimal> ingredientQuantity : ingredientsQuantity
          .entrySet()) {
        ingredientQuantity.setValue(ingredientQuantity.getValue()
            .multiply(BigDecimal.valueOf(restrictionTypeCount.getValue())));
      }
      MapUtil.merge(reservationIngredientsQuantity, ingredientsQuantity);
    }
    return reservationIngredientsQuantity;
  }
}
