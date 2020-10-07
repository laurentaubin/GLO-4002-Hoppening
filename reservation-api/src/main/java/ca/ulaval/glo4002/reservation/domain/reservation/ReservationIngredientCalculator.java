package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.util.MapUtil;
import ca.ulaval.glo4002.reservation.infra.inmemory.MenuRepository;

public class ReservationIngredientCalculator {
  private final MenuRepository menuRepository;

  public ReservationIngredientCalculator(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public Map<IngredientName, Double> getReservationIngredientsQuantity(Reservation reservation) {
    Map<RestrictionType, Integer> reservationRestrictionTypeCount = reservation.getRestrictionTypeCount();
    Map<IngredientName, Double> reservationIngredientsQuantity = new HashMap<>();

    for (Map.Entry<RestrictionType, Integer> restrictionTypeCount : reservationRestrictionTypeCount.entrySet()) {
      Map<IngredientName, Double> ingredientsQuantity = menuRepository.getIngredientsQuantity(restrictionTypeCount.getKey());
      for (Map.Entry<IngredientName, Double> ingredientQuantity : ingredientsQuantity.entrySet()) {
        ingredientQuantity.setValue(ingredientQuantity.getValue()
                                    * restrictionTypeCount.getValue());
      }
      MapUtil.merge(reservationIngredientsQuantity, ingredientsQuantity);
    }
    return reservationIngredientsQuantity;
  }
}
