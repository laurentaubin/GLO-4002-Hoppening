package ca.ulaval.glo4002.reservation.domain;

import java.util.List;

public class Reservation {

  private final List<Table> tables;

  private final long id;

  public Reservation(long id, List<Table> tables) {
    this.tables = tables;
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public List<Table> getTables() {
    return tables;
  }
}
