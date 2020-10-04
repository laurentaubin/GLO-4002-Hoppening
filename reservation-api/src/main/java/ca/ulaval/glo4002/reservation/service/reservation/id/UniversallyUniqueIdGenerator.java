package ca.ulaval.glo4002.reservation.service.reservation.id;

import java.util.UUID;

public class UniversallyUniqueIdGenerator implements IdGenerator {

  public long getLongUuid() {
    // method inspired by
    // https://stackoverflow.com/questions/15184820/how-to-generate-unique-positive-long-using-uuid
    return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
  }
}
