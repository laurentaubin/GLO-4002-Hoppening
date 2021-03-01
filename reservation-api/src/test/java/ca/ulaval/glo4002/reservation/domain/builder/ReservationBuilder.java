package ca.ulaval.glo4002.reservation.domain.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.date.DinnerDate;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDate;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class ReservationBuilder {
  private static final LocalDateTime A_DINNER_DATE = LocalDateTime.now();
  private static final LocalDateTime A_RESERVATION_DATE = LocalDateTime.now();

  private ReservationId id;
  private DinnerDate dinnerDate;
  private final List<Table> tables;
  private ReservationDate reservationDate;

  public ReservationBuilder() {
    id = new ReservationId();
    dinnerDate = new DinnerDate(A_DINNER_DATE);
    tables = new ArrayList<>();
    reservationDate = new ReservationDate(A_RESERVATION_DATE);
  }

  public ReservationBuilder withId(ReservationId id) {
    this.id = id;
    return this;
  }

  public ReservationBuilder withDinnerDate(LocalDateTime dinnerDate) {
    this.dinnerDate = new DinnerDate(dinnerDate);
    return this;
  }

  public ReservationBuilder withTable(Table table) {
    tables.add(table);
    return this;
  }

  public ReservationBuilder withAnyTable() {
    Table table = new TableBuilder().withAnyCustomer().build();
    tables.add(table);
    return this;
  }

  public Reservation build() {
    return new Reservation(id, dinnerDate, tables, reservationDate);
  }
}
