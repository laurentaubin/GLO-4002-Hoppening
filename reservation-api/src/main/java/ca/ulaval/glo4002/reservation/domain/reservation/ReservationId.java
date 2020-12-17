package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.Objects;
import java.util.UUID;

public class ReservationId {
  private final String vendorCodeId;

  public ReservationId(String vendorCodeId) {
    this.vendorCodeId = vendorCodeId;
  }

  public static long generateUUID() {
    return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
  }

  public String getVendorCodeId() {
    return vendorCodeId;
  }

  @Override
  public boolean equals(Object o) {

    if (o == this)
      return true;
    if (!(o instanceof ReservationId)) {
      return false;
    }
    ReservationId reservationId = (ReservationId) o;
    return Objects.equals(vendorCodeId, reservationId.vendorCodeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendorCodeId);
  }
}
