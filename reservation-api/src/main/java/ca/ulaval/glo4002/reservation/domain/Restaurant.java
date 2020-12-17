package ca.ulaval.glo4002.reservation.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.reservation.domain.chef.ChefManager;
import ca.ulaval.glo4002.reservation.domain.exception.ForbiddenReservationException;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningConfigurationRequest;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.material.Buffet;
import ca.ulaval.glo4002.reservation.domain.material.DailyDishesQuantity;
import ca.ulaval.glo4002.reservation.domain.report.ReportPeriod;
import ca.ulaval.glo4002.reservation.domain.report.chef.NoChefsAvailableException;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.service.reservation.exception.TooManyPeopleException;

public class Restaurant {
  private static final int MAX_NUMBER_OF_CUSTOMERS_PER_DAY = 42;

  private final HoppeningEvent hoppeningEvent;
  private final ReservationFactory reservationFactory;
  private final ReservationBook reservationBook;
  private final IngredientInventory ingredientInventory;
  private final Buffet buffet;
  private final ChefManager chefManager;

  public Restaurant(ReservationFactory reservationFactory,
                    ReservationBook reservationBook,
                    IngredientInventory ingredientInventory,
                    HoppeningEvent hoppeningEvent,
                    Buffet buffet,
                    ChefManager chefManager)
  {
    this.reservationFactory = reservationFactory;
    this.reservationBook = reservationBook;
    this.ingredientInventory = ingredientInventory;
    this.hoppeningEvent = hoppeningEvent;
    this.buffet = buffet;
    this.chefManager = chefManager;
  }

  public ReservationId makeReservation(ReservationRequest reservationRequest) {
    Reservation reservation = reservationFactory.create(reservationRequest, hoppeningEvent);
    verifyReservation(reservation);
    try {
      hireChefsForNewReservation(reservation);
    } catch (NoChefsAvailableException noChefsAvailableException) {
      throw new ForbiddenReservationException();
    }
    buffet.updateDailyDishesQuantity(reservation);
    return registerReservation(reservation);
  }

  public Reservation getReservation(ReservationId reservationId) {
    return reservationBook.getReservation(reservationId);
  }

  public Map<LocalDate, DailyDishesQuantity> getDailyDishesQuantity(ReportPeriod reportPeriod) {
    return buffet.getDailyDishesQuantities(reportPeriod);
  }

  public HoppeningEvent getHoppeningEvent() {
    return hoppeningEvent;
  }

  public void configureHoppeningEvent(HoppeningConfigurationRequest hoppeningConfigurationRequest) {
    hoppeningEvent.configureHoppening(hoppeningConfigurationRequest);
  }

  private ReservationId registerReservation(Reservation reservation) {
    reservationBook.register(reservation);
    ingredientInventory.updateIngredientInventory(reservation);
    return reservation.getReservationId();
  }

  private void verifyMaximumNumberOfCustomersPerDay(Reservation reservation) {
    if (reservationBook.getNumberOfCustomersForADay(reservation.getDinnerDate())
        + reservation.getNumberOfCustomers() > MAX_NUMBER_OF_CUSTOMERS_PER_DAY)
    {
      throw new TooManyPeopleException();
    }
  }

  private void verifyAllergicConflict(Reservation reservation) {
    if (doesReservationCauseAllergicConflict(reservation)) {
      throw new ForbiddenReservationException();
    }
  }

  private void verifyIngredientAvailability(Reservation reservation) {
    if (!ingredientInventory.areAllNecessaryIngredientsAvailable(reservation,
                                                                 hoppeningEvent.getDinnerPeriod()
                                                                               .getStartDate()))
    {
      throw new ForbiddenReservationException();
    }
  }

  private void verifyReservation(Reservation reservation) {
    verifyAllergicConflict(reservation);
    verifyIngredientAvailability(reservation);
    verifyMaximumNumberOfCustomersPerDay(reservation);
  }

  private boolean doesReservationCauseAllergicConflict(Reservation reservation) {
    List<Reservation> existingReservationAtDinnerDate = reservationBook.getReservationsByDate(reservation.getDinnerDate());
    return ingredientInventory.doesReservationCauseAllergicConflict(reservation,
                                                                    existingReservationAtDinnerDate);
  }

  private void hireChefsForNewReservation(Reservation reservation) {
    List<Reservation> currentReservations = reservationBook.getReservationsByDate(reservation.getDinnerDate());
    currentReservations.add(reservation);
    chefManager.hireChefsForReservations(currentReservations);
  }

  public BigDecimal calculateTotalReservationFee() {
    BigDecimal totalFee = BigDecimal.ZERO;
    for (Reservation reservation : reservationBook.getAllReservations()) {
      totalFee = totalFee.add(reservation.getReservationFees());
    }

    return totalFee;
  }
}
