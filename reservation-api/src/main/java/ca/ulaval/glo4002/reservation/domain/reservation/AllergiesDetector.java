package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;

public class AllergiesDetector {
  private final ReservationIngredientCalculator reservationIngredientCalculator;

  public AllergiesDetector(ReservationIngredientCalculator reservationIngredientCalculator) {
    this.reservationIngredientCalculator = reservationIngredientCalculator;
  }

  public boolean isReservationAllergicFriendly(Reservation reservation,
      List<Reservation> existingReservations, Map<IngredientName, BigDecimal> dailyIngredients) {
    if (doesReservationContainAllergicCustomer(reservation)
        && doesReservationContainACustomerWithARestrictionThatContainCarrots(reservation)) {
      return false;
    }

    if (doesReservationContainAllergicCustomer(reservation)) {
      return !doPreviousReservationsContainCarrots(dailyIngredients);
    }

    if (doesReservationContainACustomerWithARestrictionThatContainCarrots(reservation)) {
      return !doPreviousReservationsContainAllergicCustomer(reservation.getDinnerDate(),
          existingReservations);
    }

    return true;
  }

  private boolean doPreviousReservationsContainCarrots(
      Map<IngredientName, BigDecimal> ingredients) {
    return ingredients.containsKey(IngredientName.CARROTS);
  }

  private boolean doesReservationContainAllergicCustomer(Reservation reservation) {
    return reservation.getRestrictionTypes().contains(RestrictionType.ALLERGIES);
  }

  private boolean doesReservationContainACustomerWithARestrictionThatContainCarrots(
      Reservation reservation) {
    Map<IngredientName, BigDecimal> ingredientQuantity =
        reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);
    return ingredientQuantity.containsKey(IngredientName.CARROTS);
  }

  private boolean doPreviousReservationsContainAllergicCustomer(LocalDateTime dinnerDate,
      List<Reservation> existingReservations) {
    List<Reservation> reservations = existingReservations.stream()
        .filter(reservation -> reservation.getDinnerDate().equals(dinnerDate))
        .collect(Collectors.toList());
    for (Reservation reservation : reservations) {
      if (reservation.getRestrictionTypes().contains(RestrictionType.ALLERGIES)) {
        return true;
      }
    }
    return false;
  }
}
