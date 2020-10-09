package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.infra.inmemory.IngredientQuantityRepository;
import ca.ulaval.glo4002.reservation.infra.inmemory.ReservationRepository;

public class AllergiesValidator {
  private final IngredientQuantityRepository ingredientQuantityRepository;
  private final ReservationIngredientCalculator reservationIngredientCalculator;
  private final ReservationRepository reservationRepository;

  public AllergiesValidator(IngredientQuantityRepository ingredientQuantityRepository,
                            ReservationIngredientCalculator reservationIngredientCalculator,
                            ReservationRepository reservationRepository)
  {
    this.ingredientQuantityRepository = ingredientQuantityRepository;
    this.reservationIngredientCalculator = reservationIngredientCalculator;
    this.reservationRepository = reservationRepository;
  }

  public boolean isReservationAllergicFriendly(Reservation reservation) {
    if (doesReservationContainAllergicCustomer(reservation)
        && doesReservationContainACustomerWithARestrictionThatContainCarrots(reservation))
    {
      return false;
    }

    if (doesReservationContainAllergicCustomer(reservation)) {
      return !doesPreviousReservationContainCarrots(reservation.getDinnerDate());
    }

    if (doesReservationContainACustomerWithARestrictionThatContainCarrots(reservation)) {
      return !doesPreviousReservationContainAllergicCustomer(reservation.getDinnerDate());
    }

    return true;
  }

  private boolean doesPreviousReservationContainCarrots(LocalDateTime dinnerDate) {
    return ingredientQuantityRepository.containsIngredientAtDate(IngredientName.CARROTS,
                                                                 dinnerDate.toLocalDate());
  }

  private boolean doesReservationContainAllergicCustomer(Reservation reservation) {
    return reservation.getRestrictionTypes().contains(RestrictionType.ALLERGIES);
  }

  private boolean doesReservationContainACustomerWithARestrictionThatContainCarrots(Reservation reservation) {
    Map<IngredientName, BigDecimal> ingredientQuantity = reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);
    return ingredientQuantity.containsKey(IngredientName.CARROTS);
  }

  private boolean doesPreviousReservationContainAllergicCustomer(LocalDateTime dinnerDate) {
    List<Reservation> reservations = reservationRepository.getReservationsByDate(dinnerDate);
    for (Reservation reservation : reservations) {
      if (reservation.getRestrictionTypes().contains(RestrictionType.ALLERGIES)) {
        return true;
      }
    }
    return false;
  }
}
