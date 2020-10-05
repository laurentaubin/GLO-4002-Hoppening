package ca.ulaval.glo4002.reservation.domain.reservation;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;

public class ReservationAuthorizer {
  private final IngredientQuantityRepository ingredientQuantityRepository;

  public ReservationAuthorizer(IngredientQuantityRepository ingredientQuantityRepository) {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
  }

  public boolean isReservationAllergicFriendly(Reservation reservation) {
    if (reservationDoesNotContainAllergicCustomer(reservation)) {
      return true;
    }

    return menuDoesNotContainCarrots(reservation);
  }

  private boolean menuDoesNotContainCarrots(Reservation reservation) {
    return !ingredientQuantityRepository.containsIngredientAtDate(IngredientName.CARROTS,
                                                                  reservation.getDinnerDate()
                                                                             .toLocalDate());
  }

  private boolean reservationDoesNotContainAllergicCustomer(Reservation reservation) {
    return !reservation.getRestrictionTypeCount().containsKey(RestrictionType.ALLERGIES);
  }
}
