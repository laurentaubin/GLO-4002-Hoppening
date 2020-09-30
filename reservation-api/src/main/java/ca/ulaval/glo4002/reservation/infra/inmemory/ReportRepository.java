package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class ReportRepository {

  private final Map<LocalDate, Map<IngredientName, Double>> ingredientsQuantityPerDay = new HashMap<>();

  private final MenuRepository menuRepository;

  public ReportRepository(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public boolean isEmpty() {
    return ingredientsQuantityPerDay.isEmpty();
  }

  public void updateIngredientsQuantity(Reservation reservation) {
    Map<RestrictionType, Integer> reservationRestrictionTypeCount = reservation.getRestrictionTypeCount();
    Map<IngredientName, Double> currentIngredientsQuantity = getCurrentIngredientsQuantity(reservation.getDinnerDate());

    Map<IngredientName, Double> reservationIngredientsQuantity = getReservationIngredientsQuantity(reservationRestrictionTypeCount);
    Map<IngredientName, Double> updatedIngredientsQuantity = mergeIngredientsQuantity(reservationIngredientsQuantity,
                                                                                      currentIngredientsQuantity);
    ingredientsQuantityPerDay.replace(LocalDate.from(reservation.getDinnerDate()),
                                      updatedIngredientsQuantity);
  }

  public Map<IngredientName, Double> getIngredientsQuantity(LocalDateTime date) {
    return getCurrentIngredientsQuantity(date);
  }

  private Map<IngredientName, Double> getReservationIngredientsQuantity(Map<RestrictionType, Integer> reservationRestrictionTypeCount) {
    Map<IngredientName, Double> reservationIngredientsQuantity = new HashMap<>();

    for (Map.Entry<RestrictionType, Integer> restrictionTypeCount : reservationRestrictionTypeCount.entrySet()) {
      Map<IngredientName, Double> ingredientsQuantity = menuRepository.getIngredientsQuantity(restrictionTypeCount.getKey());
      for (Map.Entry<IngredientName, Double> ingredientQuantity : ingredientsQuantity.entrySet()) {
        ingredientQuantity.setValue(ingredientQuantity.getValue()
                                    * restrictionTypeCount.getValue());
      }
      reservationIngredientsQuantity = mergeIngredientsQuantity(reservationIngredientsQuantity,
                                                                ingredientsQuantity);
    }
    return reservationIngredientsQuantity;
  }

  private Map<IngredientName, Double> getCurrentIngredientsQuantity(LocalDateTime date) {
    if (ingredientsQuantityPerDay.containsKey(LocalDate.from(date))) {
      return ingredientsQuantityPerDay.get(LocalDate.from(date));
    }
    ingredientsQuantityPerDay.put(LocalDate.from(date), new HashMap<>());
    return ingredientsQuantityPerDay.get(LocalDate.from(date));
  }

  private Map<IngredientName, Double> mergeIngredientsQuantity(Map<IngredientName, Double> currentIngredientInformation,
                                                               Map<IngredientName, Double> ingredientInformationForRestriction)
  {
    Map<IngredientName, Double> mergedMap = new HashMap<>(currentIngredientInformation);
    for (Map.Entry<IngredientName, Double> entry : ingredientInformationForRestriction.entrySet()) {
      if (currentIngredientInformation.containsKey(entry.getKey())) {
        mergedMap.put(entry.getKey(),
                      currentIngredientInformation.get(entry.getKey()) + entry.getValue());
      } else {
        mergedMap.put(entry.getKey(), entry.getValue());
      }
    }
    return mergedMap;
  }
}
