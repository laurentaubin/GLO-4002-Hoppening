package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.List;

import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDate;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDateFactory;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDate;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDateFactory;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;

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
