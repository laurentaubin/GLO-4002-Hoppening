package ca.ulaval.glo4002.reservation.domain;

import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class ReservationFactory {
  private final DinnerDateFactory dinnerDateFactory;
  private final ReservationDateFactory reservationDateFactory;
  private final TableFactory tableFactory;

  public ReservationFactory(DinnerDateFactory dinnerDateFactory,
                            ReservationDateFactory reservationDateFactory,
                            TableFactory tableRequestFactory)
  {
    this.dinnerDateFactory = dinnerDateFactory;
    this.reservationDateFactory = reservationDateFactory;
    this.tableFactory = tableRequestFactory;
  }

  public Reservation create(ReservationRequest reservationRequest, HoppeningEvent hoppeningEvent) {
    ReservationId reservationId = new ReservationId();
    DinnerDate dinnerDate = dinnerDateFactory.create(reservationRequest.getDinnerDate(),
                                                     hoppeningEvent.getDinnerPeriod());
    ReservationDate reservationDate = reservationDateFactory.create(reservationRequest.getReservationDate(),
                                                                    hoppeningEvent.getReservationPeriod());
    List<Table> tables = tableFactory.createTables(reservationRequest.getTables());
    return new Reservation(reservationId, dinnerDate, tables, reservationDate);
  }
}
