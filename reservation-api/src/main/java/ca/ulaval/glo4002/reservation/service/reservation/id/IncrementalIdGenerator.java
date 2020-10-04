package ca.ulaval.glo4002.reservation.service.reservation.id;

public class IncrementalIdGenerator implements IdGenerator {
  private static long counter = 0;

  public long getLongUuid() {
    return counter++;
  }
}
