package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;

public class DinnerDate {
  private final LocalDateTime dinnerDate;

  public DinnerDate(LocalDateTime dinnerDate) {
    this.dinnerDate = dinnerDate;
  }

  public LocalDateTime getLocalDateTime() {
    return dinnerDate;
  }
}