package ca.ulaval.glo4002.reservation.domain.reservation;

public class ReservationIdFactory {
  public ReservationId createFromVendorCode(String vendorCode) {
    long uniqueId = ReservationId.generateUUID();
    return new ReservationId(vendorCode + "-" + uniqueId);
  }

  public ReservationId createFromExistingId(String vendorCode) {
    return new ReservationId(vendorCode);
  }
}
