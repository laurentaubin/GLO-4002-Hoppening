package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;

public class ReservationAuthorizer {
  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final ReservationIngredientCalculator reservationIngredientCalculator;

  public ReservationAuthorizer(IngredientQuantityRepository ingredientQuantityRepository,
                               ReservationIngredientCalculator reservationIngredientCalculator)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.reservationIngredientCalculator = reservationIngredientCalculator;
  }

  public boolean isReservationAllergicFriendly(Reservation reservation) {
    if (reservationDoesNotContainAllergicCustomer(reservation)) {
      return true;
    }

    return !menuContainsCarrots(reservation) && !reservationContainsCarrots(reservation);
  }

  private boolean reservationContainsCarrots(Reservation reservation) {
    Map<IngredientName, Double> ingredientQuantity = reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);
    return ingredientQuantity.containsKey(IngredientName.CARROTS);
  }

  private boolean menuContainsCarrots(Reservation reservation) {
    return ingredientQuantityRepository.containsIngredientAtDate(IngredientName.CARROTS,
                                                                 reservation.getDinnerDate()
                                                                            .toLocalDate());
  }

  private boolean reservationDoesNotContainAllergicCustomer(Reservation reservation) {
    return !reservation.getRestrictionTypeCount().containsKey(RestrictionType.ALLERGIES);
  }
}
