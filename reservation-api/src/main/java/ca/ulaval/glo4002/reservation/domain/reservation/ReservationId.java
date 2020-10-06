package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.Objects;
import java.util.UUID;

public class ReservationId {
  private final long id;

  public ReservationId() {
    id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
  }

  public ReservationId(long reservationId) {
    id = reservationId;
  }

  public long getLongId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {

    if (o == this)
      return true;
    if (!(o instanceof ReservationId)) {
      return false;
    }
    ReservationId reservationId = (ReservationId) o;
    return Objects.equals(id, reservationId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
