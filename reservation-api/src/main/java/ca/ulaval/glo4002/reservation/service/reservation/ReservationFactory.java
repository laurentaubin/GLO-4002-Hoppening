package ca.ulaval.glo4002.reservation.service.reservation;

import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDate;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDateFactory;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDate;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDateFactory;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationIdFactory;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;
import java.util.List;

public class ReservationFactory {
  private final DinnerDateFactory dinnerDateFactory;
  private final ReservationDateFactory reservationDateFactory;
  private final TableFactory tableFactory;
  private final ReservationIdFactory reservationIdFactory;

  public ReservationFactory(DinnerDateFactory dinnerDateFactory,
                            ReservationDateFactory reservationDateFactory,
                            TableFactory tableRequestFactory,
                            ReservationIdFactory reservationIdFactory)
  {
    this.dinnerDateFactory = dinnerDateFactory;
    this.reservationDateFactory = reservationDateFactory;
    this.tableFactory = tableRequestFactory;
    this.reservationIdFactory = reservationIdFactory;
  }

  public Reservation create(ReservationRequest reservationRequest, HoppeningEvent hoppeningEvent) {
    ReservationId reservationId = reservationIdFactory.createFromVendorCode(reservationRequest.getVendorCode());
    DinnerDate dinnerDate = dinnerDateFactory.create(reservationRequest.getDinnerDate(), hoppeningEvent.getDinnerPeriod());
    ReservationDate
      reservationDate =
      reservationDateFactory.create(reservationRequest.getReservationDate(), hoppeningEvent.getReservationPeriod());
    List<Table> tables = tableFactory.createTables(reservationRequest.getTables());
    return new Reservation(reservationId, dinnerDate, tables, reservationDate);
  }
}
