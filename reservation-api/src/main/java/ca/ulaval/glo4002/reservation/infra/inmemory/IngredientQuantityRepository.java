package ca.ulaval.glo4002.reservation.infra.inmemory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.fullcourse.IngredientName;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationIngredientCalculator;
import ca.ulaval.glo4002.reservation.domain.util.MapUtil;

public class IngredientQuantityRepository {

  private final Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantityPerDay = new HashMap<>();

  private final ReservationIngredientCalculator reservationIngredientCalculator;

  public IngredientQuantityRepository(ReservationIngredientCalculator reservationIngredientCalculator) {
    this.reservationIngredientCalculator = reservationIngredientCalculator;
  }

  public boolean isEmpty() {
    return ingredientsQuantityPerDay.isEmpty();
  }

  public void updateIngredientsQuantity(Reservation reservation) {
    Map<IngredientName, BigDecimal> currentIngredientsQuantity = getIngredientsQuantity(LocalDate.from(reservation.getDinnerDate()));

    Map<IngredientName, BigDecimal> reservationIngredientsQuantity = reservationIngredientCalculator.getReservationIngredientsQuantity(reservation);
    Map<IngredientName, BigDecimal> updatedIngredientsQuantity = MapUtil.merge(reservationIngredientsQuantity,
                                                                               currentIngredientsQuantity);
    ingredientsQuantityPerDay.put(LocalDate.from(reservation.getDinnerDate()),
                                  updatedIngredientsQuantity);
  }

  public Map<IngredientName, BigDecimal> getIngredientsQuantity(LocalDate date) {
    if (ingredientsQuantityPerDay.containsKey(date)) {
      return ingredientsQuantityPerDay.get(date);
    }
    ingredientsQuantityPerDay.put(date, new HashMap<>());
    return ingredientsQuantityPerDay.get(date);
  }

  public Map<LocalDate, Map<IngredientName, BigDecimal>> getIngredientsQuantity(ReportPeriod reportPeriod) {
    Map<LocalDate, Map<IngredientName, BigDecimal>> ingredientsQuantity = new HashMap<>();
    for (LocalDate date : reportPeriod.getAllDaysOfPeriod()) {
      if (ingredientsQuantityPerDay.containsKey(date)) {
        ingredientsQuantity.put(date, ingredientsQuantityPerDay.get(date));
      }
    }
    return ingredientsQuantity;
  }
}
