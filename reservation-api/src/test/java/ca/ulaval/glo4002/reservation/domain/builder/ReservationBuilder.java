package ca.ulaval.glo4002.reservation.domain.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;

public class ReservationBuilder {
  private static final int A_ID = 12345;
  private long id;
  private final List<Table> tables;

  public ReservationBuilder() {
    id = A_ID;
    tables = new ArrayList<>();
  }

  public ReservationBuilder withId(long id) {
    this.id = id;
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
    return new Reservation(id, tables);
  }
}
